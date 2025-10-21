package org.github.arosecra.book.pipeline.book.steps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.book.pipeline.util.Images;

public class CreateOCRPropertiesStep implements JobStep {
	private static List<String> LABELS = Arrays.asList(new String[] { "Text", "Image", "Blank", "Exclude" });
	private static List<Color> COLORS = Arrays.asList(new Color[] { Color.GREEN, Color.WHITE, Color.RED, Color.BLACK });

	private JFrame frame;
	private JPanel thumbnailPanel;

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		CompletableFuture<int[]> future = new CompletableFuture<>();
		File[] imgFiles = job.tempFolder.listFiles();

		BufferedImage[] thumbnails = createThumbnails(imgFiles);

		System.out.println("Creating GUI");

		setupUI(future, thumbnails);
		displayUI();

		int[] imgTypes = future.join();
		System.out.println("Creating properties");

		Properties props = createProperties(imgFiles, imgTypes);

		try (OutputStream os = new BufferedOutputStream(
				new FileOutputStream(new File(job.destFolder, "ocr.properties")))) {
			props.store(os, "OCR Configuration");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Properties createProperties(File[] imgFiles, int[] res) {
		Properties props = new Properties();

		int blankTypeNo = LABELS.indexOf("Blank");
		int imageTypeNo = LABELS.indexOf("Image");
		int excludeTypeNo = LABELS.indexOf("Exclude");

		String blankPages = "";
		String imagePages = "";
		String excludePages = "";

		for (int i = 0; i < res.length; i++) {
			int imageType = res[i];
			String filename = imgFiles[i].getName();
			if (imageType == blankTypeNo) {
				blankPages += filename + ",";
			} else if (imageType == imageTypeNo) {
				imagePages += filename + ",";
			} else if (imageType == excludeTypeNo) {
				excludePages += filename + ",";
			}
		}
		props.put("blankPages", blankPages);
		props.put("imagePages", imagePages);
		props.put("excludePages", excludePages);
		return props;
	}

	private void displayUI() {
		frame.pack();
		frame.setVisible(true);
	}

	private void setupUI(CompletableFuture<int[]> future, BufferedImage[] thumbnails) {
		int[] imgTypes = new int[thumbnails.length];
		createUI(thumbnails, imgTypes);
//		addUIOnClick(imgTypes);
		addOnClose(imgTypes, future);
	}

	private void addOnClose(int[] imgTypes, CompletableFuture<int[]> future) {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				future.complete(imgTypes);
			}
		});
	}

//	private void addUIOnClick(int[] imgTypes) {
//		thumbnailPanel.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				Component target = e.getComponent().getComponentAt(e.getPoint());
//				if (target instanceof JLabel) {
//					JLabel label = (JLabel) target;
//
//					int thumbnailIdx = Integer.parseInt(label.getName());
//					String text = label.getText();
//					int idx = LABELS.indexOf(text) + 1;
//					if (idx % LABELS.size() == 0) {
//						idx = 0;
//					}
//					text = LABELS.get(idx);
//					label.setBackground(COLORS.get(idx));
//					label.setText(text);
//					imgTypes[thumbnailIdx] = idx;
//
//				}
//			}
//		});
//	}

	private void createUI(BufferedImage[] thumbnails, int[] imgTypes) {
		frame = new JFrame("Thumbnails");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		thumbnailPanel = new JPanel();
		thumbnailPanel.setLayout(new GridLayout(0, 8, 5, 5)); // 4 columns, auto rows, 5px gap

		for (int i = 0; i < thumbnails.length; i++) {
			ImageIcon thumbnailIcon = new ImageIcon(thumbnails[i]);
			JLabel thumbnailLabel = new JLabel(thumbnailIcon);
			thumbnailLabel.setText(LABELS.get(0));
			thumbnailLabel.setBackground(COLORS.get(0));
			thumbnailLabel.setOpaque(true);
			thumbnailLabel.setName(i + "");
			thumbnailLabel.setHorizontalTextPosition(JLabel.CENTER);
			thumbnailLabel.setVerticalTextPosition(JLabel.BOTTOM);

			thumbnailLabel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					int thumbnailIdx = Integer.parseInt(thumbnailLabel.getName());
					String text = thumbnailLabel.getText();
					int idx = LABELS.indexOf(text) + 1;
					if (idx % LABELS.size() == 0) {
						idx = 0;
					}
					text = LABELS.get(idx);
					thumbnailLabel.setBackground(COLORS.get(idx));
					thumbnailLabel.setText(text);
					imgTypes[thumbnailIdx] = idx;
				}

			});

			thumbnailPanel.add(thumbnailLabel);
		}

		JScrollPane scrollPane = new JScrollPane(thumbnailPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		frame.add(scrollPane, BorderLayout.CENTER);
	}

	private BufferedImage[] createThumbnails(File[] imgFiles) {
		BufferedImage[] thumbnails = new BufferedImage[imgFiles.length];
		for (int i = 0; i < imgFiles.length; i++) {
			try {
				BufferedImage originalImage = ImageIO.read(imgFiles[i]);
				thumbnails[i] = Images.resizeImageToWidth(originalImage, 200, Image.SCALE_FAST);
				if (i > 0 && i % 50 == 0) {
					System.out.println(i + " / " + imgFiles.length + " thumbnails prepared.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return thumbnails;
	}

}

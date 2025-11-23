

export class Image {
	// static async cropImageInMemory(imageSrc, cropX, cropY, cropWidth, cropHeight) {
	// 	// 1. Load the image
	// 	const image = new Image();
	// 	image.src = imageSrc;
	// 	await image.decode(); // Wait for image to load

	// 	// 2. Create a standard canvas element, but don't append it to the DOM
	// 	const canvas = document.createElement('canvas');
	// 	canvas.width = cropWidth;
	// 	canvas.height = cropHeight;
	// 	const ctx = canvas.getContext('2d');

	// 	// 3. Draw the cropped portion
	// 	ctx.drawImage(
	// 		image,
	// 		cropX, cropY, cropWidth, cropHeight,
	// 		0, 0, cropWidth, cropHeight
	// 	);

	// 	// 4. Get the cropped image data (e.g., as a data URL or Blob)
	// 	// For large images, prefer toBlob() for performance
	// 	return new Promise(resolve => {
	// 		canvas.toBlob(resolve, 'image/jpeg', 0.92); // Specify format and quality
	// 	});
	// }


// Example usage:
// cropImageOffscreen('my-image.jpg', 50, 50, 200, 150)
//   .then(blob => {
//     // Use the resulting blob, e.g., for upload or display
//     const url = URL.createObjectURL(blob);
//     console.log('Cropped image URL:', url);
//   });

}
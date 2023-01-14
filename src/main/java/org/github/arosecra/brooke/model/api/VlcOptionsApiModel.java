package org.github.arosecra.brooke.model.api;

public class VlcOptionsApiModel {
	private int audioTrack;
	private int subtitleTrack;
	public int getAudioTrack() {
		return audioTrack;
	}
	public int getSubtitleTrack() {
		return subtitleTrack;
	}
	public void setAudioTrack(int audioTrack) {
		this.audioTrack = audioTrack;
	}
	public void setSubtitleTrack(int subtitleTrack) {
		this.subtitleTrack = subtitleTrack;
	}
}
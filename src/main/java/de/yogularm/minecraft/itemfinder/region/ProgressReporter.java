package de.yogularm.minecraft.itemfinder.region;

public class ProgressReporter implements ProgressListener {
	private ProgressListener listener;
	private double currentProgress = 0;
	
	public ProgressReporter(ProgressListener listener) {
		this.listener = listener;
	}
	
	public void onAction(String action) {
		listener.onAction(action);
	}
	
	public void onProgress(double progress) {
		currentProgress = progress;
		listener.onProgress(progress);
	}
	
	public void incProgress(double increment) {
		onProgress(currentProgress + increment);
	}
	
	public ProgressListener startSubtask(final double weight) {
		final double progressOnStart = currentProgress;
		return new ProgressListener() {
			@Override
			public void onProgress(double progress) {
				ProgressReporter.this.onProgress(progressOnStart + progress * weight);
			}

			@Override
			public void onAction(String action) {
				listener.onAction(action);
			}
		};
	}
}

package de.yogularm.minecraft.itemfinder.region;

public interface ProgressListener {
	void onAction(String action);
	void onProgress(double progress);
}

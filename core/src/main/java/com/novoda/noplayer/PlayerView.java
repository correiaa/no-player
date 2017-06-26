package com.novoda.noplayer;

import android.view.View;

import com.novoda.noplayer.model.TextCues;

public interface PlayerView {

    View getContainerView();

    SurfaceHolderRequester getSurfaceHolderRequester();

    Player.VideoSizeChangedListener getVideoSizeChangedListener();

    Player.StateChangedListener getStateChangedListener();

    void showSubtitles();

    void hideSubtitles();

    void setSubtitleCue(TextCues textCues);
}

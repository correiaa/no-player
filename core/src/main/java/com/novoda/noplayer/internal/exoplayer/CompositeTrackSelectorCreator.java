package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerVideoTrackSelector;

class CompositeTrackSelectorCreator {

    private final DefaultBandwidthMeter bandwidthMeter;

    CompositeTrackSelectorCreator(DefaultBandwidthMeter bandwidthMeter) {
        this.bandwidthMeter = bandwidthMeter;
    }

    CompositeTrackSelector create(Options options) {
        TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(
                bandwidthMeter,
                options.maxInitialBitrate(),
                options.minDurationBeforeQualityIncreaseInMillis(),
                AdaptiveTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS,
                AdaptiveTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,
                AdaptiveTrackSelection.DEFAULT_BANDWIDTH_FRACTION,
                AdaptiveTrackSelection.DEFAULT_BUFFERED_FRACTION_TO_LIVE_EDGE_FOR_QUALITY_INCREASE,
                AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS,
                Clock.DEFAULT
        );
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

        ExoPlayerTrackSelector exoPlayerTrackSelector = ExoPlayerTrackSelector.newInstance(trackSelector);
        FixedTrackSelection.Factory trackSelectionFactory = new FixedTrackSelection.Factory();
        ExoPlayerAudioTrackSelector audioTrackSelector = new ExoPlayerAudioTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);
        ExoPlayerVideoTrackSelector videoTrackSelector = new ExoPlayerVideoTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);
        ExoPlayerSubtitleTrackSelector subtitleTrackSelector = new ExoPlayerSubtitleTrackSelector(
                exoPlayerTrackSelector,
                trackSelectionFactory
        );
        return new CompositeTrackSelector(trackSelector, audioTrackSelector, videoTrackSelector, subtitleTrackSelector);
    }

}



package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.utlis;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


import com.wastatus.savestory.statussaver.directmessage.savemedia.R;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;


public class VideoControllerView extends FrameLayout {
    private static final String TAG = "VideoControllerView";
    
    private MediaPlayerControl mediaPlayerControl;
    private final Context context;
    private ViewGroup viewGroup;
    private View root;
    private ProgressBar progressBar;
    private TextView tvEndTime, tvCurrentTime;
    private boolean isShowing;
    private boolean isDragging;
    private static final int    sDefaultTimeout = 3000;
    private static final int    FADE_OUT = 1;
    private static final int    SHOW_PROGRESS = 2;
    private final boolean isFastForward;
    private boolean isFromXml;
    private boolean isSetListener;
    private OnClickListener nextListener, preListener;
    StringBuilder formatBuilder;
    Formatter formatter;
    private ImageView ivPause;
    private ImageView ivForward;
    private ImageView ivRewind;
    private ImageView ivNext;
    private ImageView ivPrevious;
    private ImageView ivFullScreen;
    private final Handler handler = new MessageHandler(this);

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        root = null;
        this.context = context;
        isFastForward = true;
        isFromXml = true;
        
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context, boolean useFastForward) {
        super(context);
        this.context = context;
        isFastForward = useFastForward;
        
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context) {
        this(context, true);

        Log.i(TAG, TAG);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (root != null)
            initControllerView(root);
    }
    
    public void setMediaPlayer(MediaPlayerControl player) {
        mediaPlayerControl = player;
        updatePausePlay();
        updateFullScreen();
    }


    public void setAnchorView(ViewGroup view) {
        viewGroup = view;

        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflate.inflate(R.layout.media_controller, null);

        initControllerView(root);

        return root;
    }

    private void initControllerView(View v) {
        ivPause = v.findViewById(R.id.pause);
        if (ivPause != null) {
            ivPause.requestFocus();
            ivPause.setOnClickListener(mPauseListener);
        }
        
        ivFullScreen = v.findViewById(R.id.fullscreen);
        if (ivFullScreen != null) {
            ivFullScreen.requestFocus();
            ivFullScreen.setOnClickListener(mFullscreenListener);
        }

        ivForward = v.findViewById(R.id.ivForward);
        if (ivForward != null) {
            ivForward.setOnClickListener(fwdListener);
            if (!isFromXml) {
                ivForward.setVisibility(isFastForward ? View.VISIBLE : View.GONE);
            }
        }

        ivRewind = v.findViewById(R.id.rew);
        if (ivRewind != null) {
            ivRewind.setOnClickListener(mRewListener);
            if (!isFromXml) {
                ivRewind.setVisibility(isFastForward ? View.VISIBLE : View.GONE);
            }
        }

        ivNext = v.findViewById(R.id.next);
        if (ivNext != null && !isFromXml && !isSetListener) {
            ivNext.setVisibility(View.GONE);
        }
        ivPrevious = v.findViewById(R.id.prev);
        if (ivPrevious != null && !isFromXml && !isSetListener) {
            ivPrevious.setVisibility(View.GONE);
        }

        progressBar = v.findViewById(R.id.seekbar);
        if (progressBar != null) {
            if (progressBar instanceof SeekBar) {
                SeekBar seeker = (SeekBar) progressBar;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            progressBar.setMax(1000);
        }

        tvEndTime = v.findViewById(R.id.time);
        tvCurrentTime = v.findViewById(R.id.time_current);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());

        installPrevNextListeners();
    }

    public void show() {
        show(sDefaultTimeout);
    }


    private void disableUnsupportedButtons() {
        if (mediaPlayerControl == null) {
            return;
        }
        
        try {
            if (ivPause != null && !mediaPlayerControl.canPause()) {
                ivPause.setEnabled(false);
            }
            if (ivRewind != null && !mediaPlayerControl.canSeekBackward()) {
                ivRewind.setEnabled(false);
            }
            if (ivForward != null && !mediaPlayerControl.canSeekForward()) {
                ivForward.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }
    
    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     * @param timeout The timeout in milliseconds. Use 0 to show
     * the controller until hide() is called.
     */
    public void show(int timeout) {
        if (!isShowing && viewGroup != null) {
            setProgress();
            if (ivPause != null) {
                ivPause.requestFocus();
            }
            disableUnsupportedButtons();

            LayoutParams tlp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            );
            
            viewGroup.addView(this, tlp);
            isShowing = true;
        }
        updatePausePlay();
        updateFullScreen();
        
        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        handler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = handler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            handler.removeMessages(FADE_OUT);
            handler.sendMessageDelayed(msg, timeout);
        }
    }
    
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (viewGroup == null) {
            return;
        }

        try {
            viewGroup.removeView(this);
            handler.removeMessages(SHOW_PROGRESS);
        } catch (IllegalArgumentException ex) {
            Log.w("MediaController", "already removed");
        }
        isShowing = false;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mediaPlayerControl == null || isDragging) {
            return 0;
        }
        
        int position = mediaPlayerControl.getCurrentPosition();
        int duration = mediaPlayerControl.getDuration();
        if (progressBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                progressBar.setProgress( (int) pos);
            }
            int percent = mediaPlayerControl.getBufferPercentage();
            progressBar.setSecondaryProgress(percent * 10);
        }

        if (tvEndTime != null)
            tvEndTime.setText(stringForTime(duration));
        if (tvCurrentTime != null)
            tvCurrentTime.setText(stringForTime(position));

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mediaPlayerControl == null) {
            return true;
        }
        
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (ivPause != null) {
                    ivPause.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mediaPlayerControl.isPlaying()) {
                mediaPlayerControl.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mediaPlayerControl.isPlaying()) {
                mediaPlayerControl.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            if(mediaPlayerControl.isPlaying())
            {
                mediaPlayerControl.stop();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private final OnClickListener mPauseListener = new OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private final OnClickListener mFullscreenListener = new OnClickListener() {
        public void onClick(View v) {
            doToggleFullscreen();
            show(sDefaultTimeout);
        }
    };

    public void updatePausePlay() {
        if (root == null || ivPause == null || mediaPlayerControl == null) {
            return;
        }

        if (mediaPlayerControl.isPlaying()) {
            ivPause.setImageResource(R.drawable.ic_media_pause);
        } else {
            ivPause.setImageResource(R.drawable.ic_media_play);
        }
    }

    public void updateFullScreen() {
        if (root == null || ivFullScreen == null || mediaPlayerControl == null) {
            return;
        }
        
        if (mediaPlayerControl.isFullScreen()) {
            ivFullScreen.setImageResource(R.drawable.ic_media_fullscreen_shrink);
        }
        else {
            ivFullScreen.setImageResource(R.drawable.ic_media_fullscreen_stretch);
        }
    }

    private void doPauseResume() {
        if (mediaPlayerControl == null) {
            return;
        }
        
        if (mediaPlayerControl.isPlaying()) {
            mediaPlayerControl.pause();
        } else {
            mediaPlayerControl.start();
        }
        updatePausePlay();
    }

    private void doToggleFullscreen() {
        if (mediaPlayerControl == null) {
            return;
        }
        
        mediaPlayerControl.toggleFullScreen();
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the position of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private final OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            isDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            handler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
            if (mediaPlayerControl == null) {
                return;
            }
            
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mediaPlayerControl.getDuration();
            long position = (duration * progress) / 1000L;
            mediaPlayerControl.seekTo( (int) position);
            if (tvCurrentTime != null)
                tvCurrentTime.setText(stringForTime( (int) position));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            isDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            handler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (ivPause != null) {
            ivPause.setEnabled(enabled);
        }
        if (ivForward != null) {
            ivForward.setEnabled(enabled);
        }
        if (ivRewind != null) {
            ivRewind.setEnabled(enabled);
        }
        if (ivNext != null) {
            ivNext.setEnabled(enabled && nextListener != null);
        }
        if (ivPrevious != null) {
            ivPrevious.setEnabled(enabled && preListener != null);
        }
        if (progressBar != null) {
            progressBar.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    private final OnClickListener mRewListener = new OnClickListener() {
        public void onClick(View v) {
            if (mediaPlayerControl == null) {
                return;
            }
            
            int pos = mediaPlayerControl.getCurrentPosition();
            pos -= 5000; // milliseconds
            mediaPlayerControl.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private final OnClickListener fwdListener = new OnClickListener() {
        public void onClick(View v) {
            if (mediaPlayerControl == null) {
                return;
            }
            
            int pos = mediaPlayerControl.getCurrentPosition();
            pos += 15000; // milliseconds
            mediaPlayerControl.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private void installPrevNextListeners() {
        if (ivNext != null) {
            ivNext.setOnClickListener(nextListener);
            ivNext.setEnabled(nextListener != null);
        }

        if (ivPrevious != null) {
            ivPrevious.setOnClickListener(preListener);
            ivPrevious.setEnabled(preListener != null);
        }
    }

    public void setPrevNextListeners(OnClickListener next, OnClickListener prev) {
        nextListener = next;
        preListener = prev;
        isSetListener = true;

        if (root != null) {
            installPrevNextListeners();
            
            if (ivNext != null && !isFromXml) {
                ivNext.setVisibility(View.VISIBLE);
            }
            if (ivPrevious != null && !isFromXml) {
                ivPrevious.setVisibility(View.VISIBLE);
            }
        }
    }
    
    public interface MediaPlayerControl {
        void    start();
        void    pause();
        int     getDuration();
        int     getCurrentPosition();
        void    seekTo(int pos);
        boolean isPlaying();
        int     getBufferPercentage();
        boolean canPause();
        boolean canSeekBackward();
        boolean canSeekForward();

        void stop();

        boolean isFullScreen();
        void    toggleFullScreen();
    }
    
    private static class MessageHandler extends Handler {
        private final WeakReference<VideoControllerView> mView; 

        MessageHandler(VideoControllerView view) {
            mView = new WeakReference<VideoControllerView>(view);
        }
        @Override
        public void handleMessage(Message msg) {
            VideoControllerView view = mView.get();
            if (view == null || view.mediaPlayerControl == null) {
                return;
            }
            
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    view.hide();
                    break;
                case SHOW_PROGRESS:
                        pos = view.setProgress();
                        if (!view.isDragging && view.isShowing && view.mediaPlayerControl.isPlaying()) {
                            msg = obtainMessage(SHOW_PROGRESS);
                            sendMessageDelayed(msg, 1000 - (pos % 1000));
                        }
                    break;
            }
        }
    }
}
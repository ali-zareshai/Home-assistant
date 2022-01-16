package com.bistun.homeassistant.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bistun.homeassistant.R
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class VideoActivity : AppCompatActivity() {
//    private val url = "rtsp://rtsp:12345678@172.16.24.16:554/av_stream/ch0"

    private var libVlc: LibVLC? = null
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoLayout: VLCVideoLayout
    private var url:String ?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        libVlc = LibVLC(this)
        mediaPlayer = MediaPlayer(libVlc)
        videoLayout = findViewById(R.id.videoLayout)

        url=intent.getStringExtra("endpoint").toString()
        Log.e("video_url:", url!!)
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer.attachViews(videoLayout, null, false, false)
        val media = Media(libVlc, Uri.parse(url!!))
        media.setHWDecoderEnabled(true, false)
        media.addOption(":network-caching=600")
        mediaPlayer.setMedia(media)
        media.release()
        mediaPlayer.play()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        mediaPlayer.detachViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        libVlc!!.release()
    }
}
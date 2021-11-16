package com.application.venturaapp.helper


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object Helper {

    val TAG = Helper::class.java.simpleName

    fun isOnline(): Boolean {
        try {
            val timeoutMs = 5000
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()

            return true
        } catch (e: IOException) {
            return false
        }
    }

    fun getBitmap(context: Context, drawable: Int, width: Int, height: Int): Bitmap {
        val icon = BitmapFactory.decodeResource(context.resources, drawable)
        return Bitmap.createScaledBitmap(icon, width, height, false)
    }
/*
    fun getMarkerBitmap(context: Context, drawable: Int): Bitmap {
        val icon = BitmapFactory.decodeResource(context.resources, drawable)
        return Bitmap.createScaledBitmap(icon, 100, 100, false)
    }

    fun bitMapToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun getDate(): String {
        val dfDate = SimpleDateFormat("dd/MM/yyyy")
        val date = dfDate.format(Calendar.getInstance().getTime())
        val dfTime = SimpleDateFormat("HH:mm:ss")
        val time = dfTime.format(Calendar.getInstance().getTime())
        return "$date - $time"
    }
*/
    fun getItemList(ayudaListaDato: List<AyudaListaDato>): MutableList<String> {
        val items = mutableListOf<String>()
        for (v in ayudaListaDato)
            items.add(v.nombre)
        return items
    }
/*
    fun getIdsList(ayudaListaDato: List<AyudaListaDato>): MutableList<Int> {
        val items = mutableListOf<Int>()
        for (v in ayudaListaDato)
            items.add(v.id)
        return items
    }

    fun configAudioInmediata(mediaPlayer: MediaPlayer, psb_play: PlaySeekBar) {
        psb_play.visibility = View.VISIBLE
        val seekBar = psb_play.findViewById<SeekBar>(R.id.skb_play)
        psb_play.setPlayOnclickListener(object : View.OnClickListener {
            val handler = Handler()
            var isPreparing = false
            override fun onClick(v: View?) {
                seekBar.progress = 0
                if (mediaPlayer.isPlaying) {
                    handler.removeCallbacksAndMessages(null)
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                    mediaPlayer.start()
                    updatSeekBar()
                    return;
                }
                handler.removeCallbacksAndMessages(null)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.seekTo(0)
                    mediaPlayer.start()
                    seekBar.max = it.duration / 1000
                    updatSeekBar()
                }
                mediaPlayer.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                    override fun onCompletion(mp: MediaPlayer?) {
                        mp?.stop()
                    }
                })
                mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
                    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                        Snackbar.make(psb_play, "Ha ocurrido un error", Snackbar.LENGTH_LONG).show()
                        return true
                    }
                })
            }

            fun updatSeekBar() {
                val updateSeekbar = object : Runnable {
                    override fun run() {
                        val progress = mediaPlayer.currentPosition / 1000 + 1
                        seekBar.progress = progress
                        if (progress < seekBar.max) {
                            handler.postDelayed(this, 1000)
                        }
                    }
                }
                handler.postDelayed(updateSeekbar, 0)
            }
        })
    }
*/
    fun listToMap(l: List<String>): LinkedHashMap<String, String> {
        val map = linkedMapOf<String, String>()
        l.forEach {
            val s = it.split("|")
            map[s[0]] = s[1]
        }
        return map
    }
/*
    fun uriToBase64(uri: Uri, context: Context): String {
        val inputData = readBytes(context, uri)
        return Base64.encodeToString(inputData, Base64.DEFAULT)
    }

    @Throws(IOException::class)
    private fun readBytes(context: Context, uri: Uri): ByteArray? =
            context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun base64toBitmap(encodedImage: String?): Bitmap {
        val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte
    }

    fun showAlertDialog(context: Context?, title: String, message: String) {
        context?.also {
            val builder = AlertDialog.Builder(it)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("De acuerdo", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                        }
                    })
            val alert = builder.create()
            alert.show()
        }
    }

    fun stringToMillis(dateString: String): Long {
        val format = SimpleDateFormat("dd/MM/yyyy")
        var date: Date = Calendar.getInstance().time
        try {
            date = format.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }*/
}


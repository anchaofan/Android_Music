package com.llw.music.adapter;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.llw.music.R;
import com.llw.music.model.Song;
import com.llw.music.utils.MusicUtils;

import java.util.List;
import java.util.Random;

public class MusicListAdapter extends BaseQuickAdapter<Song, BaseViewHolder>{

    //定义播放模式
    private boolean singlePlay=false;//单曲循环
    private boolean randomPlay=false;//随机播放
    private boolean sequencePlay=false;//顺序播放
    public  static MediaPlayer mediaPlayer;
    private static int pos;
    private List<Song> mList;//歌曲列表


    public MusicListAdapter(int layoutResId, @Nullable List<Song> data) {
        super(layoutResId, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, Song item) {
        //给控件赋值
        int duration = item.duration;
        String time = MusicUtils.formatTime(duration);

        helper.setText(R.id.tv_song_name,item.getSong().trim())//歌曲名称
                .setText(R.id.tv_singer,item.getSinger()+" - "+item.getAlbum())//歌手 - 专辑
                .setText(R.id.tv_duration_time,time)//歌曲时间
                //歌曲序号，因为getAdapterPosition得到的位置是从0开始，故而加1，
                //是因为位置和1都是整数类型，直接赋值给TextView会报错，故而拼接了""
                .setText(R.id.tv_position,helper.getAdapterPosition()+1+"");
        helper.addOnClickListener(R.id.item_music);//给item添加点击事件，点击之后传递数据到播放页面或者在本页面进行音乐播放


    }

    //设置单曲循环
    public void setSinglePlay(){
        singlePlay=true;
        randomPlay=false;
        sequencePlay=false;
    }

    //设置随机播放
    public void setRandomPlay(){
        singlePlay=false;
        randomPlay=true;
        sequencePlay=false;
    }

    //设置顺序播放
    public void setSequencePlay(){
        singlePlay=false;
        randomPlay=false;
        sequencePlay=true;
    }

    public int deleteMusic() {
        return pos;
    }

    public void setMediaPlayer(int position){

            pos=position;
            //重置MediaPlayer
            mediaPlayer.reset();
            //让MediaPlayer处于准备状态,异步准备资源防止卡顿
            mediaPlayer.prepareAsync();
            //调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            //对音乐播放状态进行监听，如果播放完毕就播放下一首
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(sequencePlay==true) {
                        nextMusic();
                    }else if(singlePlay==true){
                        setMediaPlayer(pos);
                    }else if(randomPlay==true){
                        Random random=new Random();
                        int i=random.nextInt(mList.size());
                        setMediaPlayer(i);
                    }else {
                        nextMusic();
                    }
                }

                private void nextMusic() {
                }
            });

    }
}

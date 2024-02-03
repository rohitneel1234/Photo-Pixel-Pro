package com.rohitneel.photopixelpro.photocollage.assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StickerFileAsset {

    public static List<String> amojiList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/amoji/01.webp");
        arrayList.add("stickers/amoji/02.webp");
        arrayList.add("stickers/amoji/03.webp");
        arrayList.add("stickers/amoji/04.webp");
        arrayList.add("stickers/amoji/05.webp");
        arrayList.add("stickers/amoji/06.webp");
        arrayList.add("stickers/amoji/07.webp");
        arrayList.add("stickers/amoji/08.webp");
        arrayList.add("stickers/amoji/09.webp");
        arrayList.add("stickers/amoji/10.webp");
        arrayList.add("stickers/amoji/11.webp");
        arrayList.add("stickers/amoji/12.webp");
        arrayList.add("stickers/amoji/13.webp");
        arrayList.add("stickers/amoji/14.webp");
        arrayList.add("stickers/amoji/15.webp");
        arrayList.add("stickers/amoji/16.webp");
        arrayList.add("stickers/amoji/17.webp");
        arrayList.add("stickers/amoji/18.webp");
        arrayList.add("stickers/amoji/19.webp");
        arrayList.add("stickers/amoji/20.webp");
        arrayList.add("stickers/amoji/21.webp");
        arrayList.add("stickers/amoji/22.webp");
        arrayList.add("stickers/amoji/23.webp");
        arrayList.add("stickers/amoji/24.webp");
        arrayList.add("stickers/amoji/25.webp");
        arrayList.add("stickers/amoji/26.webp");
        arrayList.add("stickers/amoji/27.webp");
        arrayList.add("stickers/amoji/28.webp");
        arrayList.add("stickers/amoji/29.webp");
        arrayList.add("stickers/amoji/30.webp");
  /*      arrayList.add("stickers/amoji/31.webp");
        arrayList.add("stickers/amoji/32.webp");
        arrayList.add("stickers/amoji/33.webp");
        arrayList.add("stickers/amoji/34.webp");
        arrayList.add("stickers/amoji/35.webp");
        arrayList.add("stickers/amoji/36.webp");
        arrayList.add("stickers/amoji/37.webp");
        arrayList.add("stickers/amoji/38.webp");
        arrayList.add("stickers/amoji/39.webp");
        arrayList.add("stickers/amoji/40.webp");
        arrayList.add("stickers/amoji/41.webp");
        arrayList.add("stickers/amoji/42.webp");
        arrayList.add("stickers/amoji/43.webp");
        arrayList.add("stickers/amoji/44.webp");
        arrayList.add("stickers/amoji/45.webp");
        arrayList.add("stickers/amoji/46.webp");*/
        return arrayList;
    }

    public static List<String> bubbleList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/bubble/01.webp");
        arrayList.add("stickers/bubble/02.webp");
        arrayList.add("stickers/bubble/03.webp");
        arrayList.add("stickers/bubble/04.webp");
        arrayList.add("stickers/bubble/05.webp");
        arrayList.add("stickers/bubble/06.webp");
        arrayList.add("stickers/bubble/07.webp");
        arrayList.add("stickers/bubble/08.webp");
        arrayList.add("stickers/bubble/09.webp");
        arrayList.add("stickers/bubble/10.webp");
        arrayList.add("stickers/bubble/11.webp");
        arrayList.add("stickers/bubble/12.webp");
        arrayList.add("stickers/bubble/13.webp");
        arrayList.add("stickers/bubble/14.webp");
        arrayList.add("stickers/bubble/15.webp");
        arrayList.add("stickers/bubble/16.webp");
        arrayList.add("stickers/bubble/17.webp");
        arrayList.add("stickers/bubble/18.webp");
        arrayList.add("stickers/bubble/19.webp");
        arrayList.add("stickers/bubble/20.webp");
        arrayList.add("stickers/bubble/21.webp");
        arrayList.add("stickers/bubble/22.webp");
        arrayList.add("stickers/bubble/23.webp");
        arrayList.add("stickers/bubble/24.webp");
        arrayList.add("stickers/bubble/25.webp");
        arrayList.add("stickers/bubble/26.webp");
        arrayList.add("stickers/bubble/27.webp");
        arrayList.add("stickers/bubble/28.webp");
        return arrayList;
    }

    public static List<String> rageList() {
        List<String> arrayList = new ArrayList<>();
        /*arrayList.add("stickers/rage/01.webp");
        arrayList.add("stickers/rage/02.webp");
        arrayList.add("stickers/rage/03.webp");
        arrayList.add("stickers/rage/04.webp");
        arrayList.add("stickers/rage/05.webp");
        arrayList.add("stickers/rage/06.webp");
        arrayList.add("stickers/rage/07.webp");
        arrayList.add("stickers/rage/08.webp");
        arrayList.add("stickers/rage/09.webp");
        arrayList.add("stickers/rage/10.webp");*/
      /*  arrayList.add("stickers/rage/11.webp");
        arrayList.add("stickers/rage/12.webp");
        arrayList.add("stickers/rage/13.webp");
        arrayList.add("stickers/rage/14.webp");
        arrayList.add("stickers/rage/15.webp");
        arrayList.add("stickers/rage/16.webp");
        arrayList.add("stickers/rage/17.webp");
        arrayList.add("stickers/rage/18.webp");
        arrayList.add("stickers/rage/19.webp");
        arrayList.add("stickers/rage/20.webp");
        arrayList.add("stickers/rage/21.webp");
        arrayList.add("stickers/rage/22.webp");
        arrayList.add("stickers/rage/23.webp");
        arrayList.add("stickers/rage/24.webp");
        arrayList.add("stickers/rage/25.webp");
        arrayList.add("stickers/rage/26.webp");
        arrayList.add("stickers/rage/27.webp");
        arrayList.add("stickers/rage/28.webp");
        arrayList.add("stickers/rage/29.webp");
        arrayList.add("stickers/rage/30.webp");
        arrayList.add("stickers/rage/31.webp");
        arrayList.add("stickers/rage/32.webp");
        arrayList.add("stickers/rage/33.webp");
        arrayList.add("stickers/rage/34.webp");
        arrayList.add("stickers/rage/35.webp");
        arrayList.add("stickers/rage/36.webp");
        arrayList.add("stickers/rage/37.webp");
        arrayList.add("stickers/rage/38.webp");
        arrayList.add("stickers/rage/39.webp");
        arrayList.add("stickers/rage/40.webp");
        arrayList.add("stickers/rage/41.webp");
        arrayList.add("stickers/rage/42.webp");
        arrayList.add("stickers/rage/43.webp");
        arrayList.add("stickers/rage/44.webp");
        arrayList.add("stickers/rage/45.webp");
        arrayList.add("stickers/rage/46.webp");
        arrayList.add("stickers/rage/47.webp");
        arrayList.add("stickers/rage/48.webp");
        arrayList.add("stickers/rage/49.webp");
        arrayList.add("stickers/rage/50.webp");
        arrayList.add("stickers/rage/51.webp");
        arrayList.add("stickers/rage/52.webp");
        arrayList.add("stickers/rage/53.webp");
        arrayList.add("stickers/rage/54.webp");
        arrayList.add("stickers/rage/55.webp");
        arrayList.add("stickers/rage/56.webp");
        arrayList.add("stickers/rage/57.webp");
        arrayList.add("stickers/rage/58.webp");
        arrayList.add("stickers/rage/59.webp");
        arrayList.add("stickers/rage/60.webp");
        arrayList.add("stickers/rage/61.webp");
        arrayList.add("stickers/rage/62.webp");
        arrayList.add("stickers/rage/63.webp");
        arrayList.add("stickers/rage/64.webp");
        arrayList.add("stickers/rage/65.webp");
        arrayList.add("stickers/rage/66.webp");
        arrayList.add("stickers/rage/67.webp");
        arrayList.add("stickers/rage/68.webp");
        arrayList.add("stickers/rage/69.webp");
        arrayList.add("stickers/rage/70.webp");
        arrayList.add("stickers/rage/71.webp");
        arrayList.add("stickers/rage/72.webp");
        arrayList.add("stickers/rage/73.webp");
        arrayList.add("stickers/rage/74.webp");
        arrayList.add("stickers/rage/75.webp");
        arrayList.add("stickers/rage/76.webp");
        arrayList.add("stickers/rage/77.webp");
        arrayList.add("stickers/rage/78.webp");
        arrayList.add("stickers/rage/79.webp");
        arrayList.add("stickers/rage/80.webp");
        arrayList.add("stickers/rage/81.webp");
        arrayList.add("stickers/rage/82.webp");
        arrayList.add("stickers/rage/83.webp");
        arrayList.add("stickers/rage/84.webp");
        arrayList.add("stickers/rage/85.webp");
        arrayList.add("stickers/rage/86.webp");
        arrayList.add("stickers/rage/87.webp");
        arrayList.add("stickers/rage/88.webp");
        arrayList.add("stickers/rage/89.webp");
        arrayList.add("stickers/rage/90.webp");
        arrayList.add("stickers/rage/91.webp");
        arrayList.add("stickers/rage/92.webp");
        arrayList.add("stickers/rage/93.webp");
        arrayList.add("stickers/rage/94.webp");
        arrayList.add("stickers/rage/95.webp");
        arrayList.add("stickers/rage/96.webp");
        arrayList.add("stickers/rage/97.webp");
        arrayList.add("stickers/rage/98.webp");
        arrayList.add("stickers/rage/99.webp");*/
        return arrayList;
    }

   /* public static List<String> cartoonList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/cartoon/01.webp");
        arrayList.add("stickers/cartoon/02.webp");
        arrayList.add("stickers/cartoon/03.webp");
        arrayList.add("stickers/cartoon/04.webp");
        arrayList.add("stickers/cartoon/05.webp");
        arrayList.add("stickers/cartoon/06.webp");
        arrayList.add("stickers/cartoon/07.webp");
        arrayList.add("stickers/cartoon/08.webp");
        arrayList.add("stickers/cartoon/09.webp");
        arrayList.add("stickers/cartoon/10.webp");
        arrayList.add("stickers/cartoon/11.webp");
        arrayList.add("stickers/cartoon/12.webp");
        arrayList.add("stickers/cartoon/13.webp");
        arrayList.add("stickers/cartoon/14.webp");
        arrayList.add("stickers/cartoon/15.webp");
        arrayList.add("stickers/cartoon/16.webp");
        arrayList.add("stickers/cartoon/17.webp");
        arrayList.add("stickers/cartoon/18.webp");
        arrayList.add("stickers/cartoon/19.webp");
        arrayList.add("stickers/cartoon/20.webp");
        arrayList.add("stickers/cartoon/21.webp");
        arrayList.add("stickers/cartoon/22.webp");
        arrayList.add("stickers/cartoon/23.webp");
        arrayList.add("stickers/cartoon/24.webp");
        arrayList.add("stickers/cartoon/25.webp");
        arrayList.add("stickers/cartoon/26.webp");
        arrayList.add("stickers/cartoon/27.webp");
        arrayList.add("stickers/cartoon/28.webp");
        arrayList.add("stickers/cartoon/29.webp");
        arrayList.add("stickers/cartoon/30.webp");
        return arrayList;
    }*/

    public static List<String> christmasList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/christmas/01.webp");
        arrayList.add("stickers/christmas/02.webp");
        arrayList.add("stickers/christmas/03.webp");
        arrayList.add("stickers/christmas/04.webp");
        arrayList.add("stickers/christmas/05.webp");
        arrayList.add("stickers/christmas/06.webp");
        arrayList.add("stickers/christmas/07.webp");
        arrayList.add("stickers/christmas/08.webp");
        arrayList.add("stickers/christmas/09.webp");
        arrayList.add("stickers/christmas/10.webp");
        arrayList.add("stickers/christmas/11.webp");
        arrayList.add("stickers/christmas/12.webp");
        arrayList.add("stickers/christmas/13.webp");
        arrayList.add("stickers/christmas/14.webp");
        arrayList.add("stickers/christmas/15.webp");
        arrayList.add("stickers/christmas/16.webp");
        arrayList.add("stickers/christmas/17.webp");
        arrayList.add("stickers/christmas/18.webp");
        arrayList.add("stickers/christmas/19.webp");
        arrayList.add("stickers/christmas/20.webp");
        arrayList.add("stickers/christmas/21.webp");
        arrayList.add("stickers/christmas/22.webp");
        arrayList.add("stickers/christmas/23.webp");
        arrayList.add("stickers/christmas/24.webp");
        arrayList.add("stickers/christmas/25.webp");
        arrayList.add("stickers/christmas/26.webp");
        return arrayList;
    }

    public static List<String> childList() {
        List<String> arrayList = new ArrayList<>();
        /*arrayList.add("stickers/child/01.webp");
        arrayList.add("stickers/child/02.webp");
        arrayList.add("stickers/child/03.webp");
        arrayList.add("stickers/child/04.webp");
        arrayList.add("stickers/child/05.webp");*/
        /*arrayList.add("stickers/child/06.webp");
        arrayList.add("stickers/child/07.webp");
        arrayList.add("stickers/child/08.webp");
        arrayList.add("stickers/child/09.webp");
        arrayList.add("stickers/child/10.webp");
        arrayList.add("stickers/child/11.webp");
        arrayList.add("stickers/child/12.webp");
        arrayList.add("stickers/child/13.webp");
        arrayList.add("stickers/child/14.webp");
        arrayList.add("stickers/child/15.webp");
        arrayList.add("stickers/child/16.webp");
        arrayList.add("stickers/child/17.webp");
        arrayList.add("stickers/child/18.webp");
        arrayList.add("stickers/child/19.webp");
        arrayList.add("stickers/child/20.webp");
        arrayList.add("stickers/child/21.webp");
        arrayList.add("stickers/child/22.webp");
        arrayList.add("stickers/child/23.webp");
        arrayList.add("stickers/child/24.webp");
        arrayList.add("stickers/child/25.webp");
        arrayList.add("stickers/child/26.webp");
        arrayList.add("stickers/child/27.webp");
        arrayList.add("stickers/child/28.webp");*/
        return arrayList;
    }

    public static List<String> deliciousList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/delicious/01.webp");
        arrayList.add("stickers/delicious/02.webp");
        arrayList.add("stickers/delicious/03.webp");
        arrayList.add("stickers/delicious/04.webp");
        arrayList.add("stickers/delicious/05.webp");
        arrayList.add("stickers/delicious/06.webp");
        arrayList.add("stickers/delicious/07.webp");
        arrayList.add("stickers/delicious/08.webp");
        arrayList.add("stickers/delicious/09.webp");
       /* arrayList.add("stickers/delicious/10.webp");
        arrayList.add("stickers/delicious/11.webp");
        arrayList.add("stickers/delicious/12.webp");
        arrayList.add("stickers/delicious/13.webp");
        arrayList.add("stickers/delicious/14.webp");*/
/*        arrayList.add("stickers/delicious/15.webp");
        arrayList.add("stickers/delicious/16.webp");
        arrayList.add("stickers/delicious/17.webp");
        arrayList.add("stickers/delicious/18.webp");
        arrayList.add("stickers/delicious/19.webp");
        arrayList.add("stickers/delicious/20.webp");*/
        return arrayList;
    }

    public static List<String> emojList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/emoj/13.webp");
        arrayList.add("stickers/emoj/14.webp");
        arrayList.add("stickers/emoj/15.webp");
        arrayList.add("stickers/emoj/16.webp");
        arrayList.add("stickers/emoj/17.webp");
        arrayList.add("stickers/emoj/18.webp");
        arrayList.add("stickers/emoj/19.webp");
        arrayList.add("stickers/emoj/20.webp");
        arrayList.add("stickers/emoj/21.webp");
        arrayList.add("stickers/emoj/22.webp");
        arrayList.add("stickers/emoj/23.webp");
        arrayList.add("stickers/emoj/24.webp");
        arrayList.add("stickers/emoj/25.webp");
        arrayList.add("stickers/emoj/26.webp");
        arrayList.add("stickers/emoj/27.webp");
        arrayList.add("stickers/emoj/28.webp");
        arrayList.add("stickers/emoj/29.webp");
        arrayList.add("stickers/emoj/30.webp");
        return arrayList;
    }

    public static List<String> flowerList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/flower/01.webp");
        arrayList.add("stickers/flower/02.webp");
        arrayList.add("stickers/flower/03.webp");
        arrayList.add("stickers/flower/04.webp");
        arrayList.add("stickers/flower/05.webp");
        arrayList.add("stickers/flower/06.webp");
        arrayList.add("stickers/flower/07.webp");
        arrayList.add("stickers/flower/08.webp");
        /*arrayList.add("stickers/flower/09.webp");
        arrayList.add("stickers/flower/10.webp");
        arrayList.add("stickers/flower/11.webp");
        arrayList.add("stickers/flower/12.webp");
        arrayList.add("stickers/flower/13.webp");
        arrayList.add("stickers/flower/14.webp");
        arrayList.add("stickers/flower/15.webp");*/
        return arrayList;
    }

    public static List<String> handList() {
        List<String> arrayList = new ArrayList<>();
        //arrayList.add("stickers/hand/01.webp");
        /*arrayList.add("stickers/hand/02.webp");
        arrayList.add("stickers/hand/03.webp");
        arrayList.add("stickers/hand/04.webp");
        arrayList.add("stickers/hand/05.webp");
        arrayList.add("stickers/hand/06.webp");
        arrayList.add("stickers/hand/07.webp");
        arrayList.add("stickers/hand/08.webp");
        arrayList.add("stickers/hand/09.webp");
       // arrayList.add("stickers/hand/10.webp");
        arrayList.add("stickers/hand/11.webp");
        arrayList.add("stickers/hand/12.webp");
        arrayList.add("stickers/hand/13.webp");
        arrayList.add("stickers/hand/14.webp");
        arrayList.add("stickers/hand/15.webp");
        arrayList.add("stickers/hand/16.webp");
        arrayList.add("stickers/hand/17.webp");
        arrayList.add("stickers/hand/18.webp");
        arrayList.add("stickers/hand/19.webp");
        arrayList.add("stickers/hand/20.webp");
        arrayList.add("stickers/hand/21.webp");
        arrayList.add("stickers/hand/22.webp");
        arrayList.add("stickers/hand/23.webp");*/
       /* arrayList.add("stickers/hand/24.webp");
        arrayList.add("stickers/hand/25.webp");*/
        return arrayList;
    }

    public static List<String> popularList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/popular/01.webp");
        arrayList.add("stickers/popular/02.webp");
        arrayList.add("stickers/popular/03.webp");
        arrayList.add("stickers/popular/04.webp");
        arrayList.add("stickers/popular/05.webp");
        arrayList.add("stickers/popular/06.webp");
        arrayList.add("stickers/popular/07.webp");
        arrayList.add("stickers/popular/08.webp");
       /* arrayList.add("stickers/popular/09.webp");
        arrayList.add("stickers/popular/10.webp");
        arrayList.add("stickers/popular/11.webp");
        arrayList.add("stickers/popular/12.webp");
        arrayList.add("stickers/popular/13.webp");
        arrayList.add("stickers/popular/14.webp");
        arrayList.add("stickers/popular/15.webp");*/
     /*   arrayList.add("stickers/popular/16.webp");
        arrayList.add("stickers/popular/17.webp");
        arrayList.add("stickers/popular/18.webp");*/
        return arrayList;
    }

    public static List<String> rainbowList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/rainbow/01.webp");
        arrayList.add("stickers/rainbow/02.webp");
        arrayList.add("stickers/rainbow/03.webp");
        arrayList.add("stickers/rainbow/04.webp");
        arrayList.add("stickers/rainbow/05.webp");
        arrayList.add("stickers/rainbow/06.webp");
        arrayList.add("stickers/rainbow/07.webp");
        arrayList.add("stickers/rainbow/08.webp");
        arrayList.add("stickers/rainbow/09.webp");
        arrayList.add("stickers/rainbow/10.webp");
        arrayList.add("stickers/rainbow/11.webp");
        arrayList.add("stickers/rainbow/12.webp");
       /* arrayList.add("stickers/rainbow/13.webp");
        arrayList.add("stickers/rainbow/14.webp");
        arrayList.add("stickers/rainbow/15.webp");
        arrayList.add("stickers/rainbow/16.webp");
        arrayList.add("stickers/rainbow/17.webp");
        arrayList.add("stickers/rainbow/18.webp");
        arrayList.add("stickers/rainbow/19.webp");
        arrayList.add("stickers/rainbow/20.webp");
        arrayList.add("stickers/rainbow/21.webp");*/
        return arrayList;
    }

    public static List<String> stickerList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/sticker/01.webp");
        arrayList.add("stickers/sticker/02.webp");
        arrayList.add("stickers/sticker/03.webp");
        arrayList.add("stickers/sticker/04.webp");
        arrayList.add("stickers/sticker/05.webp");
        arrayList.add("stickers/sticker/06.webp");
        arrayList.add("stickers/sticker/07.webp");
        arrayList.add("stickers/sticker/08.webp");
        arrayList.add("stickers/sticker/09.webp");
        arrayList.add("stickers/sticker/10.webp");
        arrayList.add("stickers/sticker/11.webp");
        arrayList.add("stickers/sticker/12.webp");
        arrayList.add("stickers/sticker/13.webp");
        arrayList.add("stickers/sticker/14.webp");
        arrayList.add("stickers/sticker/15.webp");
        arrayList.add("stickers/sticker/16.webp");
        arrayList.add("stickers/sticker/17.webp");
        arrayList.add("stickers/sticker/18.webp");
        arrayList.add("stickers/sticker/19.webp");
        arrayList.add("stickers/sticker/20.webp");
        arrayList.add("stickers/sticker/21.webp");
        arrayList.add("stickers/sticker/22.webp");
        arrayList.add("stickers/sticker/23.webp");
        arrayList.add("stickers/sticker/24.webp");
        arrayList.add("stickers/sticker/25.webp");
        arrayList.add("stickers/sticker/26.webp");
        arrayList.add("stickers/sticker/27.webp");
        arrayList.add("stickers/sticker/28.webp");
        arrayList.add("stickers/sticker/29.webp");
        arrayList.add("stickers/sticker/30.webp");
        arrayList.add("stickers/sticker/31.webp");
        arrayList.add("stickers/sticker/32.webp");
        return arrayList;
    }

    public static List<String> unicornList() {
        List<String> arrayList = new ArrayList<>();
        /*arrayList.add("stickers/unicorn/01.webp");
        arrayList.add("stickers/unicorn/02.webp");
        arrayList.add("stickers/unicorn/03.webp");
        arrayList.add("stickers/unicorn/04.webp");
        arrayList.add("stickers/unicorn/05.webp");
        arrayList.add("stickers/unicorn/06.webp");
        arrayList.add("stickers/unicorn/07.webp");
        arrayList.add("stickers/unicorn/08.webp");
        arrayList.add("stickers/unicorn/09.webp");
        arrayList.add("stickers/unicorn/10.webp");*/
        arrayList.add("stickers/unicorn/11.webp");
        arrayList.add("stickers/unicorn/12.webp");
        arrayList.add("stickers/unicorn/13.webp");
        arrayList.add("stickers/unicorn/14.webp");
        arrayList.add("stickers/unicorn/15.webp");
        arrayList.add("stickers/unicorn/16.webp");
        arrayList.add("stickers/unicorn/17.webp");
        arrayList.add("stickers/unicorn/18.webp");
        arrayList.add("stickers/unicorn/19.webp");
        arrayList.add("stickers/unicorn/20.webp");
        arrayList.add("stickers/unicorn/21.webp");
        arrayList.add("stickers/unicorn/22.webp");
        arrayList.add("stickers/unicorn/23.webp");
        arrayList.add("stickers/unicorn/24.webp");
        arrayList.add("stickers/unicorn/25.webp");
        arrayList.add("stickers/unicorn/26.webp");
        arrayList.add("stickers/unicorn/27.webp");
        arrayList.add("stickers/unicorn/28.webp");
        arrayList.add("stickers/unicorn/29.webp");
        arrayList.add("stickers/unicorn/30.webp");
        arrayList.add("stickers/unicorn/31.webp");
        arrayList.add("stickers/unicorn/32.webp");
        arrayList.add("stickers/unicorn/33.webp");
        arrayList.add("stickers/unicorn/34.webp");
        arrayList.add("stickers/unicorn/36.webp");
        arrayList.add("stickers/unicorn/37.webp");
        arrayList.add("stickers/unicorn/38.webp");
        arrayList.add("stickers/unicorn/39.webp");
        arrayList.add("stickers/unicorn/40.webp");
        arrayList.add("stickers/unicorn/41.webp");
      /*  arrayList.add("stickers/unicorn/42.webp");
        arrayList.add("stickers/unicorn/43.webp");
        arrayList.add("stickers/unicorn/44.webp");
        arrayList.add("stickers/unicorn/45.webp");
        arrayList.add("stickers/unicorn/46.webp");
        arrayList.add("stickers/unicorn/47.webp");
        arrayList.add("stickers/unicorn/48.webp");
        arrayList.add("stickers/unicorn/49.webp");
        arrayList.add("stickers/unicorn/50.webp");*/
        arrayList.add("stickers/unicorn/69.webp");
        return arrayList;
    }

    public static List<String> valentineList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("stickers/valentine/01.webp");
        arrayList.add("stickers/valentine/02.webp");
        arrayList.add("stickers/valentine/03.webp");
        arrayList.add("stickers/valentine/04.webp");
        arrayList.add("stickers/valentine/05.webp");
        arrayList.add("stickers/valentine/06.webp");
        arrayList.add("stickers/valentine/07.webp");
        arrayList.add("stickers/valentine/08.webp");
        arrayList.add("stickers/valentine/09.webp");
        arrayList.add("stickers/valentine/10.webp");
        arrayList.add("stickers/valentine/11.webp");
        arrayList.add("stickers/valentine/12.webp");
        arrayList.add("stickers/valentine/13.webp");
        arrayList.add("stickers/valentine/14.webp");
        arrayList.add("stickers/valentine/15.webp");
        arrayList.add("stickers/valentine/16.webp");
        arrayList.add("stickers/valentine/17.webp");
     /*   arrayList.add("stickers/valentine/18.webp");
        arrayList.add("stickers/valentine/19.webp");
        arrayList.add("stickers/valentine/20.webp");
        arrayList.add("stickers/valentine/21.webp");
        arrayList.add("stickers/valentine/22.webp");
        arrayList.add("stickers/valentine/23.webp");
        arrayList.add("stickers/valentine/24.webp")*/;
        return arrayList;
    }

    public static Bitmap loadBitmapFromAssets(Context context, String str) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return decodeStream;
        } catch (Exception e) {
            return null;
        }
    }

}

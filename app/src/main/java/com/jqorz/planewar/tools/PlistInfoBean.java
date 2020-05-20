package com.jqorz.planewar.tools;

import java.util.List;

public class PlistInfoBean {

    public FramesBean frames;
    public MetadataBean metadata;


    public static class CommonBean {

        public String spriteColorRect;
        public String spriteOffset;
        public String spriteSize;
        public String spriteSourceSize;
        public boolean spriteTrimmed;
        public String textureRect;
        public boolean textureRotated;
        public List<?> aliases;

    }

    public static class FramesBean {

        public Background2Bean background_2;
        public BombBean bomb;
        public Bullet1Bean bullet1;
        public Bullet2Bean bullet2;
        public Enemy1Blowup1Bean enemy1_blowup_1;
        public Enemy1Blowup2Bean enemy1_blowup_2;
        public Enemy1Blowup3Bean enemy1_blowup_3;
        public Enemy1Blowup4Bean enemy1_blowup_4;
        public Enemy1Fly1Bean enemy1_fly_1;
        public Enemy2Blowup1Bean enemy2_blowup_1;
        public Enemy2Blowup2Bean enemy2_blowup_2;
        public Enemy2Blowup3Bean enemy2_blowup_3;
        public Enemy2Blowup4Bean enemy2_blowup_4;
        public Enemy2Blowup5Bean enemy2_blowup_5;
        public Enemy2Blowup6Bean enemy2_blowup_6;
        public Enemy2Blowup7Bean enemy2_blowup_7;
        public Enemy2Fly1Bean enemy2_fly_1;
        public Enemy2Fly2Bean enemy2_fly_2;
        public Enemy2Hit1Bean enemy2_hit_1;
        public Enemy3Blowup1Bean enemy3_blowup_1;
        public Enemy3Blowup2Bean enemy3_blowup_2;
        public Enemy3Blowup3Bean enemy3_blowup_3;
        public Enemy3Blowup4Bean enemy3_blowup_4;
        public Enemy3Fly1Bean enemy3_fly_1;
        public Enemy3Hit1Bean enemy3_hit_1;
        public Enemy4Fly1Bean enemy4_fly_1;
        public Enemy5Fly1Bean enemy5_fly_1;
        public EnemyBulletBean enemy_bullet;
        public GamePauseBean game_pause;
        public GamePauseHlBean game_pause_hl;
        public HeroBlowup1Bean hero_blowup_1;
        public HeroBlowup2Bean hero_blowup_2;
        public HeroBlowup3Bean hero_blowup_3;
        public HeroBlowup4Bean hero_blowup_4;
        public HeroFly1Bean hero_fly_1;
        public HeroFly2Bean hero_fly_2;
        public Loading0Bean loading0;
        public Loading1Bean loading1;
        public Loading2Bean loading2;
        public Loading3Bean loading3;


        public static class Background2Bean extends CommonBean {


        }

        public static class BombBean extends CommonBean {


        }

        public static class Bullet1Bean extends CommonBean {

        }

        public static class Bullet2Bean extends CommonBean {


        }

        public static class Enemy1Blowup1Bean extends CommonBean {


        }

        public static class Enemy1Blowup2Bean extends CommonBean {


        }

        public static class Enemy1Blowup3Bean extends CommonBean {


        }

        public static class Enemy1Blowup4Bean extends CommonBean {


        }

        public static class Enemy1Fly1Bean extends CommonBean {


        }

        public static class Enemy2Blowup1Bean extends CommonBean {


        }

        public static class Enemy2Blowup2Bean extends CommonBean {


        }

        public static class Enemy2Blowup3Bean extends CommonBean {


        }

        public static class Enemy2Blowup4Bean extends CommonBean {


        }

        public static class Enemy2Blowup5Bean extends CommonBean {


        }

        public static class Enemy2Blowup6Bean extends CommonBean {


        }

        public static class Enemy2Blowup7Bean extends CommonBean {


        }

        public static class Enemy2Fly1Bean extends CommonBean {


        }

        public static class Enemy2Fly2Bean extends CommonBean {


        }

        public static class Enemy2Hit1Bean extends CommonBean {


        }

        public static class Enemy3Blowup1Bean extends CommonBean {


        }

        public static class Enemy3Blowup2Bean extends CommonBean {


        }

        public static class Enemy3Blowup3Bean extends CommonBean {


        }

        public static class Enemy3Blowup4Bean extends CommonBean {


        }

        public static class Enemy3Fly1Bean extends CommonBean {


        }

        public static class Enemy3Hit1Bean extends CommonBean {


        }

        public static class Enemy4Fly1Bean extends CommonBean {


        }

        public static class Enemy5Fly1Bean extends CommonBean {


        }

        public static class EnemyBulletBean extends CommonBean {


        }

        public static class GamePauseBean extends CommonBean {


        }

        public static class GamePauseHlBean extends CommonBean {


        }

        public static class HeroBlowup1Bean extends CommonBean {


        }

        public static class HeroBlowup2Bean extends CommonBean {


        }

        public static class HeroBlowup3Bean extends CommonBean {


        }

        public static class HeroBlowup4Bean extends CommonBean {


        }

        public static class HeroFly1Bean extends CommonBean {


        }

        public static class HeroFly2Bean extends CommonBean {


        }

        public static class Loading0Bean extends CommonBean {


        }

        public static class Loading1Bean extends CommonBean {


        }

        public static class Loading2Bean extends CommonBean {


        }

        public static class Loading3Bean extends CommonBean {


        }
    }

    public static class MetadataBean extends CommonBean {
        /**
         * format : 3
         * name : gameArts
         * size : {1024, 2048}
         * target : {"coordinatesFileExtension":".plist","coordinatesFileName":"gameArts-hd","name":"default","premultipliedAlpha":false,"textureFileExtension":"","textureFileName":"gameArts-hd"}
         * version : 1.6.0
         */

        public int format;
        public String name;
        public String size;
        public TargetBean target;
        public String version;

        public static class TargetBean extends CommonBean {
            /**
             * coordinatesFileExtension : .plist
             * coordinatesFileName : gameArts-hd
             * name : default
             * premultipliedAlpha : false
             * textureFileExtension :
             * textureFileName : gameArts-hd
             */

            public String coordinatesFileExtension;
            public String coordinatesFileName;
            public String name;
            public boolean premultipliedAlpha;
            public String textureFileExtension;
            public String textureFileName;
        }
    }
}

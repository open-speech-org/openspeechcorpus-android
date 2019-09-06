package com.contraslash.android.openspeechcorpus.config;

/**
 * Created by ma0 on 11/2/15.
 */
public class Config {
    //local
    //public static String BASE_URL = "http://localhost:8000";

    //Desarrollo
    //public static String BASE_URL = "http://192.168.0.101:8000";
    public static String BASE_URL = "http://192.168.1.59:8000";


    //Produccion
//    public static String BASE_URL = "http://openspeechcorpus.contraslash.com";

    public static String API_BASE_URL = "/api";

    public static String SPLASH_SCREEN_SHOWED = "splash_screen_showed";

    public static String USER_ID = "user_id";

    public static String FILE_TO_PLAY = "file_to_play";
    public static String TEXT_IN_FILE = "text_in_file";

    public static String NAV_ITEM_ID = "nav_item_id";

    public static final long DRAWER_CLOSE_DELAY_MS = 250;


    //Suggestion Params

    public static String SUGGESTION = "suggestion";
    public static String ANONYMOUS_USER = "anonymous_user";


    public static String SHOW_TUTORIAL = "show_tutorial";
    public static String NAME_REQUESTED = "name_requested";


    //Profile vars

    public static String ANONYMOUS_USER_NAME = "anonymous_user_name";

    public static String ANONYMOUS_USER_PICTURE = "anonymous_user_picture";

    //Custon Upload Data

    public static String TEXT = "text";



    public static String STATUS_TEXT = "status";
    public static String ERROR_TEXT = "error";


    public static String ERASE_DIALOG_TAG = "erase_dialog";
    public static String FILL_NICK_DIAGLO_TAG = "fill_nick_dialog";

    public static String NEW_ID = "new_id";


    public static String COMMANDS_LAST_UPDATE = "commands_last_update";
    public static String TALES_LAST_UPDATE = "tales_last_update_new";
    public static String NEWS_LAST_UPDATE = "news_last_update";

    public static String CAPTURE_TALES_READED = "capture_tales_readed";

    public static final int PERMISSION_TO_RECORD_AUDIO = 5809;


}

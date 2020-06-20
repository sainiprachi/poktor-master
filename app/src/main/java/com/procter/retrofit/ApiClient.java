package com.procter.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.procter.application.ProcterAppController;
import com.procter.model.UserInfo;
import com.procter.session.Session;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    //local
//    public static final String BASE_URL = "http://192.168.0.131/poktor/api/pharmacyApi/login" +
//            "";
    //server
    public static final String BASE_URL = "http://52.200.8.36/poktor/";
    public static Session session;
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        session = new Session(context);

        //  Toast.makeText(context, ""+profileBean.getAuth_key(), Toast.LENGTH_SHORT).show();


        if (retrofit == null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getOKClient())
                        .build();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }


        return retrofit;
    }

    private static OkHttpClient getOKClient() throws KeyManagementException, NoSuchAlgorithmException {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.sslSocketFactory(sslSocketFactory);
        httpClient.hostnameVerifier((hostname, session) -> true);

        final UserInfo.DataBean.ProfileBean profileBean = session.getUserInfo();

        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                final UserInfo.DataBean.ProfileBean profileBean = session.getUserInfo();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Authentication", profileBean.getAuth_key())
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        return httpClient.build();
    }

    public static ApiInterface getApiService(){
        Retrofit retrofit = getClient(ProcterAppController.getInstance());
        return retrofit.create(ApiInterface.class);
    }

}



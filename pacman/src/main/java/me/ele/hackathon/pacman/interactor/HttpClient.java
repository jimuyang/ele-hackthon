package me.ele.hackathon.pacman.interactor;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by lanjiangang on 2018/12/26.
 */
public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private final String url;
    CloseableHttpClient httpclient;

    public HttpClient(String url) {

        httpclient = HttpClients.createDefault();
        this.url = url;
    }

    public void connect() throws Exception {
    }

    public byte[] send(String msg, String path) throws IOException, URISyntaxException {
        int status = 0;
        String response = "";
        byte[] content = msg.getBytes();

        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(content));
        entity.setContentLength(content.length);
        entity.setChunked(false);

        HttpPost httpPost = new HttpPost(url);
        URI uri = new URI(url + "/" + path);
        httpPost.setURI(uri);
        httpPost.setEntity(entity);

        CloseableHttpResponse res = httpclient.execute(httpPost);
        try {
            status = res.getStatusLine().getStatusCode();
            HttpEntity resEntity = res.getEntity();
            response = resEntity != null ? EntityUtils.toString(resEntity) : "";
            EntityUtils.consume(resEntity);
        } finally {
            res.close();
        }
        logger.debug("send to {}/{}: {}, response code: {} , body: {}", url, path, msg, status, response);
        return response.getBytes();
    }

}

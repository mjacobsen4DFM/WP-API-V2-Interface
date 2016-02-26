package com.DFM.Interfaces;

import com.DFM.Clients.WebClient;
import com.DFM.Clients.WordPressClient;
import com.DFM.Utils.JsonUtil;
import com.DFM.Utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mick on 2/12/2016.
 */
public class WordPressInterface {
    public static Map<String, String> postJson(String json, String postBaseEndpoint, WordPressClient wpc) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        String postLocation = postBaseEndpoint;
        String wpPostId = "";

        try {
            resultMap = wpc.post(postLocation, json);
            if (WebClient.isOK(Integer.parseInt(resultMap.get("code").trim()))) {
                wpPostId = JsonUtil.getValue(resultMap.get("result"), "id");
                postLocation = resultMap.get("location");
            }

            if (WebClient.isBad(Integer.parseInt(resultMap.get("code").trim()))) {
                resultMap = wpc.post(postLocation, json);
                if (WebClient.isBad(Integer.parseInt(resultMap.get("code").trim()))) {
                    //String errMsg = "Fatal post error for " + _subscriberMap.get("name") + " at " +  postLocation + " for contentKey: " + _contentKey + " from feedKey: " + _feedKey + " title:" + json.getTitle() + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result") + " JSON: " + JsonUtil.toJSON(json);
                    String errMsg = "Fatal post error for " + postLocation + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result") + " JSON: " + JsonUtil.toJSON(json);
                    resultMap.put("code", "500");
                    resultMap.put("error", errMsg);
                }
            }
        } catch (Exception e) {
            //String errMsg = "Fatal image post error for: " + imageName + "(" + image.getGuid() + ")" + " into Subscriber: " + _subscriberMap.get("name") + " at: " + postLocation + " for contentKey: " + _contentKey + " from feedKey: " + _feedKey + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
            String errMsg = "Fatal POST error for: " + postBaseEndpoint + " Error: " + e.getMessage() + " Cause: " + e.getCause();
            resultMap.put("code", "500");
            resultMap.put("error", errMsg);
        }
        resultMap.put("wpPostId", wpPostId);
        resultMap.put("postLocation", postLocation);
        //resultMap.put("title", JsonUtil.getValue(json, "title"));
        return resultMap;
    }

    public static Map<String, String> getPost(String postBaseEndpoint, WordPressClient wpc) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        String body;
        String postLocation = postBaseEndpoint;
        String wpPostId = "";
        try {
            body = wpc.get(postLocation);
            wpPostId = "99999"; //JsonUtil.getValue(body, "id");
            postLocation = postBaseEndpoint;

            resultMap.put("code", "200");
            resultMap.put("wpPostId", wpPostId);
            resultMap.put("postLocation", postLocation);
            resultMap.put("body", body);
        } catch (Exception e) {
            //String errMsg = "Fatal image post error for: " + imageName + "(" + image.getGuid() + ")" + " into Subscriber: " + _subscriberMap.get("name") + " at: " + postLocation + " for contentKey: " + _contentKey + " from feedKey: " + _feedKey + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
            String errMsg = "Fatal GET error for: " + postBaseEndpoint + " Error: " + e.getMessage() + " Cause: " + e.getCause();
            resultMap.put("code", "500");
            resultMap.put("error", errMsg);
        }
        return resultMap;
    }

    public static Map<String, String> postMedia(String imageJson, String mediaBaseEndpoint, WordPressClient wpc) {
        Map<String, String> resultMap = new HashMap<String, String>();
        //  String mediaEndpoint = mediaBaseEndpoint + "media/";
        String mediaLocation = "";
        String postLocation = "";
        String wpPostid = "";
        String imageName = "";
        String imageFeatured = "";
        String imageGuid = "";
        String imageSource = "";
        String imageMimetype = "";
        String imageCaption = "";
        String imageAuthor = "";
        String imageDate = "";
        String wpImageId = "";
        String imageKey = "";
        String json = "";

        try {
            //Extract image metadata
            wpPostid = JsonUtil.getValue(imageJson, "post_id");
            postLocation = JsonUtil.getValue(imageJson, "postlocation");
            imageName = JsonUtil.getValue(imageJson, "name");
            imageFeatured = JsonUtil.getValue(imageJson, "featured");
            imageSource = JsonUtil.getValue(imageJson, "source");
            imageMimetype = JsonUtil.getValue(imageJson, "mimetype");
            imageCaption = JsonUtil.getValue(imageJson, "caption");
            imageAuthor = JsonUtil.getValue(imageJson, "author");
            imageDate = JsonUtil.getValue(imageJson, "date");
            //fix name
            imageName = StringUtil.hyphenateString(imageName);

            //Upload the image
            resultMap = wpc.uploadImage(mediaBaseEndpoint, imageSource, imageMimetype, imageName);
            if (WebClient.isOK(Integer.parseInt(resultMap.get("code").trim()))) {
                wpImageId = JsonUtil.getValue(resultMap.get("result"), "id");
                mediaLocation = mediaBaseEndpoint + wpImageId;
                //recordImage(wpPostid, wpImageId, image, mediaLocation, "false");
            } else {
                String errMsg = "Final image post error for: " + imageName + "(" + imageGuid + ")" + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
                resultMap.put("code", "500");
                resultMap.put("error", errMsg);
                return resultMap;
            }

            json = "{\"id\":" + wpImageId + ",\"author\":" + imageAuthor + ",\"title\": \"" + JsonUtil.cleanString(imageName) + "\",\"date_gmt\": \"" + imageDate + "\",\"caption\": \"" + JsonUtil.cleanString(imageCaption) + "\",\"post\":" + wpPostid + "}";
            resultMap = wpc.post(mediaLocation, json);
            if (WebClient.isBad(Integer.parseInt(resultMap.get("code").trim()))) {
                resultMap = wpc.post(mediaLocation, json);
                if (WebClient.isBad(Integer.parseInt(resultMap.get("code").trim()))) {
                    String errMsg = "Final IMAGE POST error for: " + imageName + "(" + imageGuid + ")" + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
                    resultMap.put("code", "500");
                    resultMap.put("error", errMsg);
                }
            }
            if (WebClient.isOK(Integer.parseInt(resultMap.get("code").trim()))) {
                //recordImage(wpPostid, wpImageId, image, mediaLocation, "false");
                System.out.println("Upload  for: \"" + imageName + "\" at: " + postLocation);
            }

            //Add first image as featured image for post
            if (Boolean.valueOf(imageFeatured)) {
                json = "{\"id\":" + wpPostid + ",\"featured_media\":" + wpImageId + "}";
                resultMap = wpc.post(postLocation, json);
                if (WebClient.isBad(Integer.parseInt(resultMap.get("code").trim()))) {
                    resultMap = wpc.post(postLocation, json);
                    if (WebClient.isBad(Integer.parseInt(resultMap.get("code").trim()))) {
                        String errMsg = "Final IMAGE POST error for: " + imageName + "(" + imageGuid + ")" + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
                        resultMap.put("code", "500");
                        resultMap.put("error", errMsg);
                    }
                }
            }
        } catch (Exception e) {
            //String errMsg = "Fatal image post error for: " + imageName + "(" + image.getGuid() + ")" + " into Subscriber: " + _subscriberMap.get("name") + " at: " + postLocation + " for contentKey: " + _contentKey + " from feedKey: " + _feedKey + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
            String errMsg = "Fatal image post error for: " + imageName + " Error: " + e.getMessage() + " Cause: " + e.getCause();
            resultMap.put("code", "500");
            resultMap.put("error", errMsg);
        }
        resultMap.put("wpImageId", wpImageId);
        resultMap.put("mediaLocation", mediaLocation);
        return resultMap;
    }

    public static Map<String, String> postDelete(String deleteEndpoint, WordPressClient wpc) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            resultMap = wpc.delete(deleteEndpoint);
            if (WebClient.isBad(Integer.parseInt(resultMap.get("code").trim()))) {
                //String errMsg = "Final subject delete error for generic category: " + badCat + " out of Subscriber: " + _subscriberMap.get("name") + " at: " + postLocation + " for contentKey: " + _contentKey + " from feedKey: " + _feedKey + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
                String errMsg = "Fatal DELETE error for " + deleteEndpoint + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
                resultMap.put("code", "500");
                resultMap.put("error", errMsg);
            }
        } catch (Exception e) {
            //String errMsg = "Fatal image post error for: " + imageName + "(" + image.getGuid() + ")" + " into Subscriber: " + _subscriberMap.get("name") + " at: " + postLocation + " for contentKey: " + _contentKey + " from feedKey: " + _feedKey + " Code: " + resultMap.get("code") + " Response: " + resultMap.get("result");
            String errMsg = "Fatal DELETE error for: " + deleteEndpoint + " Error: " + e.getMessage() + " Cause: " + e.getCause();
            resultMap.put("code", "500");
            resultMap.put("error", errMsg);
        }
        return resultMap;
    }
}

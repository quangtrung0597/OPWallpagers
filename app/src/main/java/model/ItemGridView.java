package model;

/**
 * Created by trung on 14/11/2017.
 */

public class ItemGridView {
    private String imgUrl;
    private String smallImg;

    public ItemGridView(String imgUrl, String smallImg) {
        this.imgUrl = imgUrl;
        this.smallImg = smallImg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSmallImg() {
        return smallImg;
    }
}

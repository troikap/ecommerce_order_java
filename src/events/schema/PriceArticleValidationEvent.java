package events.schema;

public class PriceArticleValidationEvent {
    private String articleId;
    private boolean valid;
    private double price;

    public PriceArticleValidationEvent() {
    }

    public PriceArticleValidationEvent(String articleId, boolean valid, double price) {
        this.articleId = articleId;
        this.valid = valid;
        this.price = price;
    }

    public String getArticleId() {
        return articleId;
    }

    public boolean isValid() {
        return valid;
    }

    public double getPrice() {
        return price;
    }
}
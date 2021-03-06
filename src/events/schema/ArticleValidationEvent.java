package events.schema;

public class ArticleValidationEvent {
    private String articleId;
    private boolean valid;
    private int stock;

    public ArticleValidationEvent() {
    }

    public ArticleValidationEvent(String articleId, boolean valid, int stock) {
        this.articleId = articleId;
        this.valid = valid;
        this.stock = stock;
    }

    public String getArticleId() {
        return articleId;
    }

    public boolean isValid() {
        return valid;
    }

    public int getStock() {
        return stock;
    }
}
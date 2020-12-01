package events;

import java.util.Arrays;
import java.util.stream.Collectors;

import events.schema.ArticleValidationEvent;
import events.schema.PriceArticleValidationEvent;
import events.schema.Event;
import events.schema.NewArticleValidationData;
import events.schema.NewPlaceData;
import events.schema.PaymentData;
import events.schema.PlaceEvent;
import projections.ProjectionService;
import utils.errors.ValidationError;
import utils.validator.Validator;

public class EventService {
    static EventService instance;

    public static EventService getInstance() {
        if (instance == null) {
            instance = new EventService();
        }
        return instance;
    }

    public Event placeOrder(NewPlaceData data) throws ValidationError {
        Validator.validate(data);

        EventRepository repository = EventRepository.getInstance();
        Event event = repository.findPlaceByCartId(data.cartId);
        if (event == null) {
            PlaceEvent.Article[] articles = Arrays.stream(data.articles) //
                    .map(a -> new PlaceEvent.Article(a.id, a.quantity)) //
                    .collect(Collectors.toList()) //
                    .toArray(new PlaceEvent.Article[] {});

            PlaceEvent place = new PlaceEvent(data.cartId, data.userId, articles);

            event = Event.newPlaceOrder(place);
            repository.save(event);
            ProjectionService.getInstance().updateProjections(event);
        }

        return event;
    }

    public Event placeArticleExist(NewArticleValidationData data) throws ValidationError {
        Validator.validate(data);

        EventRepository repository = EventRepository.getInstance();

        Event event = Event.newArticleValidation(data.orderId,
                new ArticleValidationEvent(data.articleId, data.valid, data.stock));
        repository.save(event);

        ProjectionService.getInstance().updateProjections(event);
        return event;
    }

    public Event placePriceArticleExist(NewArticleValidationData data) throws ValidationError {
        Validator.validate(data);

        EventRepository repository = EventRepository.getInstance();

        Event event = Event.newPriceArticleValidation(data.orderId,
                new PriceArticleValidationEvent(data.articleId, data.valid, data.price));
        repository.save(event);

        ProjectionService.getInstance().updateProjections(event);
        return event;
    }

    public Event placePayment(PaymentData payment) throws ValidationError {
        Validator.validate(payment);

        Event event = Event.newPayment(payment.orderId, payment.userId, payment.method, payment.amount);
        EventRepository.getInstance().save(event);

        ProjectionService.getInstance().updateProjections(event);
        return event;
    }
}
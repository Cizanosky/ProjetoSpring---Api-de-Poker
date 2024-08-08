package api.poker.application.validator;

public interface Validator<T> {

    void validate(T t);
}

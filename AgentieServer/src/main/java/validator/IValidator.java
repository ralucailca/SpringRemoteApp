package validator;

public interface IValidator<Entity> {
    void validate(Entity entity);
}

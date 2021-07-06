package fr.insee.coleman.api.exception;

public class DuplicateResourceException extends RuntimeException {

	private static final long serialVersionUID = -2348059622111312475L;

	public DuplicateResourceException(String ressource, String id) {
		super(String.format("'%s' already exists for values : '%s'", ressource, id));
	}

}

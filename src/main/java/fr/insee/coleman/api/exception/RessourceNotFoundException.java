package fr.insee.coleman.api.exception;

public class RessourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2348059622111312475L;

	public RessourceNotFoundException(String ressource, String id) {
		super(String.format("No '%s' for value : '%s'", ressource, id));
	}

}

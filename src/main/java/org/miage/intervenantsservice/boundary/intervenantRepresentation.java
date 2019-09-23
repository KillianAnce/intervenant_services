package org.miage.intervenantsservice.boundary;

import org.miage.intervenantsservice.entity.Intervenant;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/intervenants", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Intervenant.class)
public class intervenantRepresentation {

	private final IntervenantResource ir;

	public intervenantRepresentation(IntervenantResource ir) {
		this.ir = ir;
	}

	// Get all

	@GetMapping("")
	public ResponseEntity<?> getAllIntervenants() {
		Iterable<Intervenant> allIntervenants = ir.findAll();
		return new ResponseEntity<>(allIntervenants, HttpStatus.OK);
	}

	@GetMapping(value = "/{intervenantId}")
	public ResponseEntity<?> getIntervenant(@PathVariable("intervenantId") String id) {
		return Optional.ofNullable(ir.findById(id))
				.filter(Optional::isPresent)
				.map(i -> new ResponseEntity<>(i.get(), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// POST
	@PostMapping
	public ResponseEntity<?> saveIntervenant(@RequestBody Intervenant intervenant) {
		intervenant.setId(UUID.randomUUID().toString());
		Intervenant saved = ir.save(intervenant);
		HttpHeaders responsHeaders = new HttpHeaders();
		responsHeaders.setLocation(linkTo(intervenantRepresentation.class).slash(saved.getId()).toUri());
		return new ResponseEntity<>(null, responsHeaders, HttpStatus.CREATED);
	}

	// PUT
	@PutMapping(value = "/{intervenantId}")
	public ResponseEntity<?> putIntervenant(@RequestBody Intervenant intervenant,
		@PathVariable("intervenantId") String intervenantId) {
		Optional<Intervenant> body = Optional.ofNullable(intervenant);
		if (!body.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (!ir.existsById(intervenantId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		intervenant.setId(intervenantId);
		Intervenant res = ir.save(intervenant);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// DELETE


	// Docker



	@GetMapping("/test")
	public String listIntervenants() {
		return "toto, Emilie";
	}


}

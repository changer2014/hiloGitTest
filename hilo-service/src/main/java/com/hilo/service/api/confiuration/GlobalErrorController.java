package com.hilo.service.api.confiuration;

import com.scooter.common.api.JsonResponse;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/error")
public class GlobalErrorController extends AbstractErrorController {

	/**
	 * Instantiates a new Global error controller.
	 *
	 * @param errorAttributes the error attributes
	 */
	public GlobalErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	/**
	 * Error response entity.
	 *
	 * @param request the request
	 * @return the response entity
	 */
	@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<JsonResponse<Object>> error(HttpServletRequest request) {
		HttpStatus status = getStatus(request);
		Map<String, Object> body = getErrorAttributes(request, false);
		Object obj = body.get("message");
		return new ResponseEntity<>(JsonResponse.failed(obj == null ? null : status.getReasonPhrase()), status);
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}

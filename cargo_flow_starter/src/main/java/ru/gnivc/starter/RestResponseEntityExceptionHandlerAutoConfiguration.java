/*
 * Copyright (c) 2023. Vladimir Olennikov.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.gnivc.starter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.gnivc.common.exception.AbstractException;
import ru.gnivc.common.exception.CompanyServiceException;
import ru.gnivc.common.exception.CompanyVehicleServiceException;
import ru.gnivc.common.exception.EventTypeException;
import ru.gnivc.common.exception.KeycloakClientException;
import ru.gnivc.common.exception.RealmRoleException;
import ru.gnivc.common.exception.Response;
import ru.gnivc.common.exception.RouteEventServiceException;
import ru.gnivc.common.exception.RouteLocationServiceException;
import ru.gnivc.common.exception.TaskRouteServiceException;
import ru.gnivc.common.exception.TaskServiceException;
import ru.gnivc.common.exception.UserServiceException;

@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandlerAutoConfiguration extends ResponseEntityExceptionHandler {
  private final ObjectMapper mapper;

  @ExceptionHandler(HttpStatusCodeException.class)
  protected ResponseEntity<Response> handleUserServiceException(HttpStatusCodeException e) {
    String errorMessage = extractErrorMessage(e.getMessage());
    Response response = Response.builder()
        .timestamp(LocalDateTime.now().toString())
        .error(errorMessage)
        .status((HttpStatus) e.getStatusCode())
        .build();

    return new ResponseEntity<>(response, response.getStatus());
  }

  @ExceptionHandler(EventTypeException.class)
  protected ResponseEntity<Response> handleEventTypeException(EventTypeException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(RouteEventServiceException.class)
  protected ResponseEntity<Response> handleRouteEventServiceException(
      RouteEventServiceException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(RouteLocationServiceException.class)
  protected ResponseEntity<Response> handleRouteLocationServiceException(
      RouteLocationServiceException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }


  @ExceptionHandler(TaskServiceException.class)
  protected ResponseEntity<Response> handleTaskServiceException(TaskServiceException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(TaskRouteServiceException.class)
  protected ResponseEntity<Response> handleTaskRouteServiceException(TaskRouteServiceException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(KeycloakClientException.class)
  protected ResponseEntity<Response> handleUserServiceException(KeycloakClientException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(UserServiceException.class)
  protected ResponseEntity<Response> handleUserServiceException(UserServiceException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(CompanyServiceException.class)
  protected ResponseEntity<Response> handleCompanyServiceException(CompanyServiceException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(RealmRoleException.class)
  protected ResponseEntity<Response> handleRealmRoleException(RealmRoleException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(CompanyVehicleServiceException.class)
  protected ResponseEntity<Response> handleRealmRoleException(CompanyVehicleServiceException e) {
    return new ResponseEntity<>(buildResponse(e), e.getStatus());
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<Response> handleException(AuthenticationException e) {

    Response response = Response.builder()
        .timestamp(LocalDateTime.now().toString())
        .error(e.getMessage())
        .status(HttpStatus.UNAUTHORIZED)
        .build();

    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Response> handleValidException(ConstraintViolationException e) {

    String errorString = e.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining(", "));

    Response response = Response.builder()
        .timestamp(LocalDateTime.now().toString())
        .error(errorString)
        .status(HttpStatus.BAD_REQUEST)
        .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {

    Response response = Response.builder()
        .timestamp(LocalDateTime.now().toString())
        .error(ex.getMessage())
        .status(HttpStatus.FORBIDDEN)
        .build();

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  private String extractErrorMessage(String message) {
    try {
      int jsonStart = message.indexOf("{");
      if (jsonStart == -1) {
        return message;
      }
      message = message.substring(jsonStart);
      JsonNode jsonNode = mapper.readTree(message);
      return jsonNode.path("error").asText();
    } catch (IOException ex) {
      return message;
    }
  }

  private Response buildResponse(AbstractException e) {
    return Response.builder()
        .timestamp(LocalDateTime.now().toString())
        .error(e.getMessage())
        .status(e.getStatus())
        .build();
  }
}

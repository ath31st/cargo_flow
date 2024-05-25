/*
 * Copyright (c) 2023. Vladimir Olennikov.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.gnivc.portal.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.naming.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserServiceException.class)
  protected ResponseEntity<Response> handleUserServiceException(UserServiceException e) {
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

  private Response buildResponse(AbstractException e) {
    return Response.builder()
        .timestamp(LocalDateTime.now().toString())
        .error(e.getMessage())
        .status(e.getStatus())
        .build();
  }
}

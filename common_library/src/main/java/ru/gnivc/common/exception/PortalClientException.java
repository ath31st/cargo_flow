/*
 * Copyright (c) 2023. Vladimir Olennikov.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.gnivc.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PortalClientException extends AbstractException {
  public PortalClientException(HttpStatus status, String errorMessage) {
    super(status, errorMessage);
  }
}

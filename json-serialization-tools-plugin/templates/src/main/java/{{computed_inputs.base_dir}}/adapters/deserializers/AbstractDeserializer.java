package {{base_package}}.adapters.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Optional;

public abstract class AbstractDeserializer<T> extends JsonDeserializer<T> {

    /**
     * Deserializa o objeto contido no no principal do documento JSON enviado pelo cliente.
     *
     * @param node
     *         O objeto que contem o objeto a ser deserializado.
     * @param context
     *         O contexto da deserializacao.
     * @return O objeto representado pelo parametro {@code node}.
     *
     * @throws IOException
     *         Caso ocorra algum problema na deserializacao.
     */
    public abstract T deserializeNode(JsonNode node,
                                      DeserializationContext context) throws IOException;

    /** {@inheritDoc} */
    @Override
    public T deserialize(JsonParser jsonParser,
                         DeserializationContext deserializationContext) throws IOException {
        JsonNode jsonNode = getJsonNode(jsonParser);
        return deserializeNode(jsonNode, deserializationContext);
    }

    /**
     * Retorna uma instancia de {@code JsonNode}, a partir de {@code JsonParser}.
     *
     * @param parser
     *         O objeto que efetua o <i>parsing</i> de documentos JSON.
     * @return Um objeto {@code JsonNode}, contendo a estrutura do objeto enviado pelo cliente.
     *
     * @throws IOException
     *         Caso ocorra algum problema na deserializacao.
     */
    protected JsonNode getJsonNode(JsonParser parser) throws IOException {
        ObjectCodec oc = parser.getCodec();
        return oc.readTree(parser);
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor contido em um campo com nome <i>id</i> e retorna como um
     * objeto {@code Long}.
     *
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Um objeto {@code Long} com o id associoado ou {@code null} caso nao exista esse atributo na arvore do
     *         JSON.
     */
    protected Long getId(JsonNode node) {
        if (node.hasNonNull("id")) {
            return node.get("id").asLong() != 0 ? node.get("id").asLong() : null;
        }
        return null;
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor contido em um campo com nome <i>id</i> e retorna como um
     * objeto {@code Optional}.
     *
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Um objeto {@code Optional} do tipo {@code Long} com o id associado. Caso o atributo {@code id} nao exista
     *         na arvore do JSON, uma instacia de {@code Optional#empty()} sera retornada.
     */
    protected Optional<Long> getIdAsOptional(JsonNode node) {
        if (node.hasNonNull("id")) {
            return Optional.ofNullable(node.get("id").asLong() != 0 ? node.get("id").asLong() : null);
        }
        return Optional.empty();
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code Integer}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Uma nova instancia de {@code Integer} ou {@code null} caso nao exista o atributo no documento JSON.
     */
    protected Integer getAsInt(String field,
                               JsonNode node) {
        if (node.has(field) && node.get(field).isNumber()) {
            return node.get(field).asInt();
        }
        return null;
    }

	/**
	 * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
	 * field} como um {@code Long}.
	 *
	 * @param field
	 * 		O nome do atributo a ser buscado no documento JSON.
	 * @param node
	 * 		O objeto que contem a estrutura JSON a ser deserializado.
	 * @return Uma nova instancia de {@code Long} ou {@code null} caso nao exista o atributo no documento JSON.
	 */
	protected Long getAsLong(String field,
							 JsonNode node) {
		if (node.has(field) && node.get(field).isNumber()) {
			return node.get(field).asLong();
		}
		return null;
	}

	/**
	 * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
	 * field} como um {@code Float}.
	 *
	 * @param field
	 * 		O nome do atributo a ser buscado no documento JSON.
	 * @param node
	 * 		O objeto que contem a estrutura JSON a ser deserializado.
	 * @return Uma nova instancia de {@code Float} ou {@code null} caso nao exista o atributo no documento JSON.
	 */
	protected Float getAsFloat(String field,
							 JsonNode node) {
		if (node.has(field) && node.get(field).isNumber()) {
			return (float) node.get(field).asLong();
		}
		return null;
	}

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code Double}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Uma nova instancia de {@code Double} ou {@code null} caso nao exista o atributo no documento JSON.
     */
    protected Double getAsDouble(String field,
                                 JsonNode node) {
        if (node.has(field)) {
            return node.get(field).asDouble();
        }
        return null;
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code Character}.
     *
     * @param field
     *          O nome do atributo a ser buscado no documento JSON.
     * @param node
     *          O objeto que contem a estrutura JSON a ser deserializado.
     * @return Uma nova instancia de {@code Character} ou {@code null} caso nao exista o atributo no documento JSON.
     */
    protected Character getAsChar(final String field, final JsonNode node) {
        if (node.has(field)) {
            return node.get(field).textValue().charAt(0);
        }
        return null;
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code Short}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Uma nova instancia de {@code Short} ou {@code 0} caso nao exista o atributo no documento JSON.
     */
    protected Short getAsShort(String field,
                                 JsonNode node) {
        if (node.has(field)) {
            return node.get(field).shortValue();
        }
        return null;
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code BigDecimal}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Uma nova instancia de {@code BigDecimal} ou {@code null} caso nao exista o atributo no documento JSON.
     */
    protected BigDecimal getAsBigDecimal(String field,
                                         JsonNode node) {
        if (node.hasNonNull(field)) {
            return BigDecimal.valueOf(node.get(field).asDouble());
        }
        return null;
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code BigDecimal}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Uma nova instancia de {@code BigDecimal} ou {@BigDecimal.ZERO} caso nao exista o atributo no documento
     *         JSON.
     */
    protected BigDecimal getAsBigDecimalOrZeroIfNull(String field,
                                                     JsonNode node) {
        BigDecimal asBigDecimal = getAsBigDecimal(field, node);
        if (asBigDecimal == null) {
            return BigDecimal.ZERO;
        }
        return asBigDecimal;
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como uma {@code String}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Uma {@code String} ou {@code null} caso nao exista o atributo no documento JSON.
     */
    protected String getAsString(String field,
                                 JsonNode node) {
        if (node.has(field)) {
            return node.get(field).textValue();
        }
        return null;
    }

    /**
     * Retorna o objeto {@code JsonNode} contido no no indicado no parametro {@code node}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return O no contido no no indicado no parametro {@code node}.
     */
    protected Optional<JsonNode> getAsJsonNode(String field,
                                               JsonNode node) {
        if (node.has(field)) {
            return Optional.of(node.get(field));
        }
        return Optional.empty();
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code boolean}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return Um valor <i>booleano</i> ou {@code null} caso nao exista o atributo no documento JSON.
     */
    protected Boolean getAsBoolean(String field,
                                   JsonNode node) {
        JsonNode booleanNode = node.get(field);
        return isNodeNotNull(booleanNode) ? booleanNode.asBoolean() : null;
    }

    /**
     * Retorna a partir de um objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
     * field} como um {@code byte array}.
     *
     * @param field
     *         O nome do atributo a ser buscado no documento JSON.
     * @param node
     *         O objeto que contem a estrutura JSON a ser deserializado.
     * @return O {@code byte array} contido no campo indicado no parametro {@code field}, ou {code null} caso nao exista
     *         o campo indicado no documento JSON.
     */
    protected byte[] getAsByteArray(String field,
                                    JsonNode node) throws IOException {
        if (!node.has(field)) {
            return null;
        }
        String base64 = node.get(field).asText();
        return IOUtils.toByteArray(new ByteArrayInputStream(Base64.getDecoder().decode(base64)));
    }

	/**
	 * Retorna a partir de um Objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
	 * field} como uma instancia de {@code LocalDate}.
	 *
	 * @param field
	 * 		O nome do atributo a ser buscado no documento JSON.
	 * @param node
	 * 		O objeto que contem a estrutura JSON a ser deserializado.
	 * @return Um {@code LocalDate} ou {@code null} caso nao exista o atributo no documento JSON.
	 */
	protected LocalDate getLongAsLocalDate(String field,
										   JsonNode node) throws IOException {
		if (node.has(field)) {
			return Instant.ofEpochMilli(node.get(field).asLong())
					.atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return null;
	}

	/**
	 * Retorna a partir de um Objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
	 * field} como uma instancia de {@code LocalDateTime}.
	 *
	 * @param field
	 * 		O nome do atributo a ser buscado no documento JSON.
	 * @param node
	 * 		O objeto que contem a estrutura JSON a ser deserializado.
	 * @return Um {@code LocalDateTime} ou {@code null} caso nao exista o atributo no documento JSON.
	 */
	protected LocalDateTime getLongAsLocalDateTime(String field,
												   JsonNode node) throws IOException {
		if (node.has(field)) {
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(node.get(field).asLong()), ZoneId.systemDefault());
		}
		return null;
	}

    /**
	 * Retorna a partir de um Objeto {@code JsonNode} o valor de um atributo com o nome indicado no parametro {@code
	 * field} como uma instancia de {@code Year}.
	 *
	 * @param field
	 * 		O nome do atributo a ser buscado no documento JSON.
	 * @param node
	 * 		O objeto que contem a estrutura JSON a ser deserializado.
	 * @return Um {@code Year} ou {@code null} caso nao exista o atributo no documento JSON.
	 */
	protected Year getLongAsYear(String field,
								 JsonNode node) throws IOException {
		if (node.has(field)) {
			return Year.of((int) node.get(field).asLong());
		}
		return null;
	}

    /**
     * Verifica se o no indicado no parametro {@code node} e diferente de {@code null} e se o seu tipo {@code
     * JsonNodeType} e diferente de {@code JsonNodeType.NULL}.
     *
     * @param node
     *         O objeto a ser verificado.
     * @return {@code true} se o objeto {@code JsonNode} for diferente de {@code null} e se seu tipo for diferente de
     *         {@code JsonNodeType.NULL}, ou {@code false} caso contrario.
     */
    protected boolean isNodeNotNull(JsonNode node) {
        return node != null && !node.isNull();
    }
}
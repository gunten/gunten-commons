package org.tp.mix.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.tp.exception.SysCodeMsgEnum;
import org.tp.exception.SysConstants;
import org.tp.jackson.JacksonTool;
import org.tp.mix.dto.ResultModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Predicate;

import static org.tp.jackson.JacksonTool.parseToObject;

/**
 * {@link ResultModelWrapperConverter}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see ResultModelWrapperConverter
 * 2018/12/25
 */
public class ResultModelWrapperConverter extends AbstractHttpMessageConverter<Object> {

    @Value("#{'${sys.uriwhitelist}'.split(';')}")
    private List<String> uriWhiteList;

    public ResultModelWrapperConverter() {
        super(new MediaType("application", "json", SysConstants.DEFAULT_CHARSET),
                new MediaType("application", "*+json", SysConstants.DEFAULT_CHARSET));

    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * 重写readlntenal 方法，处理请求的数据。
     */
    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage httpInputMessage) throws IOException {
        String bodyText = StreamUtils.copyToString(httpInputMessage.getBody(), SysConstants.DEFAULT_CHARSET);
        return parseToObject(bodyText, clazz);
    }

    /**
     * 重写writeInternal ，处理如何输出数据到response。
     */
    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException {
        String uri = getRequestURI();
        if (inWhiteList(uri)) {
            logger.info(String.format("URI exist in white list, do not need to convert to ResultModel: %s", uri));
            writeObjectDirectly(obj, outputMessage.getBody());
            return;
        }

        ResultModel result = getResultModelInstance(obj);
        writeValueWithResultModel(result, outputMessage.getBody());
    }

    private String getRequestURI() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request.getRequestURI();
    }

    private void writeObjectDirectly(Object obj, OutputStream out) throws IOException {
        ObjectMapper mapper = JacksonTool.getSnakeCaseObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        out.write(json.getBytes());
    }

    private boolean inWhiteList(String uri) {
        Predicate<String> p1 = item -> uri.indexOf(item) >= 0;
        return uriWhiteList.stream().anyMatch(p1);
    }

    private ResultModel getResultModelInstance(Object obj) {
        if (obj instanceof ResultModel) {
            return (ResultModel) obj;
        }

        ResultModel result = new ResultModel();
        result.setBizCode(SysCodeMsgEnum.SUCCESS.getCode());
        result.setBizMsg(SysCodeMsgEnum.SUCCESS.getMsg());
        result.setData(obj);
        return result;
    }

    private void writeValueWithResultModel(ResultModel model, OutputStream out) throws IOException {
        ObjectMapper mapper = JacksonTool.getSnakeCaseObjectMapper();
        out.write(mapper.writeValueAsString(model).getBytes());
    }

    public static JavaType getCollectionType(ObjectMapper mapper, Class<ResultModel> collectionClass,
                                             Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}

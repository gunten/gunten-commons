package org.tp.excel.poi;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

/**
 * excel的IFS公式自定义实现
 *
 * @author wuwenyang
 * @date 2023年12月14日
 */
public class IfsFunction implements FreeRefFunction {

    /**
     * 函数名
     */
    public static final String FUNCTION_NAME = "_xlfn.IFS";

    @Override
    public ValueEval evaluate(ValueEval[] valueEvals, OperationEvaluationContext operationEvaluationContext) {
        if (valueEvals == null || valueEvals.length % 2 != 0) {
            return ErrorEval.FUNCTION_NOT_IMPLEMENTED;
        }
        int length = valueEvals.length;
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                ValueEval valueEval = valueEvals[i];
                if (valueEval instanceof BoolEval) {
                    BoolEval boolEval = (BoolEval) valueEval;
                    if (boolEval.getBooleanValue()) {
                        return valueEvals[i + 1];
                    }
                }
            }
        }
        return valueEvals[length - 1];
    }
}

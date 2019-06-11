package com.soybeany.bdlib.log.extract.parser;

/**
 * 解析器
 * <br>Created by Soybeany on 2019/6/2.
 */
public interface IParser<Input, Output> {

    /**
     * 将{@link Input} 转换为 {@link Output}
     *
     * @return 转换失败时为null
     */
    Output toOutput(Input input);

    // //////////////////////////////////额外接口//////////////////////////////////

    interface ICallback<Output, Data> {
        void onSetup(Output output, Data data);
    }

    interface IOutputProvider<Output> {
        Output getNewOne();
    }

    // //////////////////////////////////默认实现//////////////////////////////////

    abstract class Impl<Param, Input, Output, Data> implements IParser<Input, Output> {
        private Param mParam;
        private ICallback<Output, Data> mCallback;
        private IOutputProvider<Output> mOutputProvider;
        private boolean mEnableNullData;
        private Output mOutput; // 重复利用的output

        public Impl(Param param, ICallback<Output, Data> callback, IOutputProvider<Output> outputProvider, boolean enableNullData, boolean recycleOutput) {
            mParam = param;
            mCallback = callback;
            mOutputProvider = outputProvider;
            mEnableNullData = enableNullData;
            if (recycleOutput) {
                mOutput = outputProvider.getNewOne();
            }
        }

        @Override
        public Output toOutput(Input input) {
            // 获得自定义用的数据
            Data data;
            try {
                data = getData(mParam, input);
            } catch (Exception e) {
                return null;
            }
            // 若不允许null数据，则直接返回
            if (null == data && !mEnableNullData) {
                return null;
            }
            // 自定义Output并返回
            Output output = mOutput;
            if (null == output) {
                output = mOutputProvider.getNewOne();
            }
            mCallback.onSetup(output, data);
            return output;
        }

        /**
         * 获得自定义用的数据
         *
         * @param param 预输入的参数
         * @param input 新输入的数据
         * @return 自定义数据，可为null
         */
        protected abstract Data getData(Param param, Input input) throws Exception;
    }
}

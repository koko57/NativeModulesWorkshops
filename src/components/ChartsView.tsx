import React, {forwardRef, Ref} from 'react';
import {
    findNodeHandle,
    requireNativeComponent,
    UIManager,
    ViewStyle,
    NativeModules,
} from 'react-native';

type CommonChartProps = {
    onValueSelected?: (event: SelectedValueEvent) => void;
    datasets: ChartDataSet[];
    style?: ViewStyle;
};

type ChartDataSet = {
    name: string;
    data: Number[];
    lineColor?: string;
    fillColor?: string;
    lineWidth?: number;
    drawFilledEnabled?: boolean;
};

type SelectedValueEvent = {
    nativeEvent: {
        datasetIndex: number;
        x: number;
        y: number;
    };
};

type NativeChartsProps = CommonChartProps & {
    RNRotationEnabled?: boolean;
    RNLegendEnabled?: boolean;
    RNWebLineWidth?: number;
    RNInnerWebLineWidth?: number;
    RNWebColor?: string;
    RNInnerWebColor?: string;
    ref: Ref<typeof NativeChartsView>;
};

type RNChartsProps = CommonChartProps & {
    rotationEnabled?: boolean;
    legendEnabled?: boolean;
    webLineWidth?: number;
    innerWebLineWidth?: number;
    webColor?: string;
    innerWebColor?: string;
};

const ComponentName = 'ChartsView';

const ChartsModule = NativeModules.ChartsModule;

export const selectValue = (
    componentOrHandle:
        | null
        | number
        | React.Component<any, any>
        | React.ComponentClass<any>,
    datasetIndex: number,
    x: number,
) => {
    UIManager.dispatchViewManagerCommand(
        findNodeHandle(componentOrHandle),
        UIManager.getViewManagerConfig(ComponentName).Commands.selectValue,
        [datasetIndex, x],
    );
};


export const enableRotation = (enable: boolean) => {
    if (ChartsModule?.enableRotation) {
        ChartsModule.enableRotation(enable);
    }
};

export const isRotationEnabled = async () => {
    if (ChartsModule?.isRotationEnabled) {
        return ChartsModule.isRotationEnabled();
    }
};


export const NativeChartsView =
    UIManager.getViewManagerConfig(ComponentName) != null
        ? requireNativeComponent<NativeChartsProps>(ComponentName)
        : () => {
            throw new Error("ChartsView doesn't exist");
        };

export const ChartsView = forwardRef<typeof NativeChartsView, RNChartsProps>(
    (props, ref) => (
        <NativeChartsView
            ref={ref}
            datasets={props.datasets}
            style={props.style}
            RNLegendEnabled={props.legendEnabled}
            RNWebLineWidth={props.webLineWidth}
            RNInnerWebLineWidth={props.innerWebLineWidth}
            RNWebColor={props.webColor}
            RNInnerWebColor={props.innerWebColor}
            onValueSelected={props.onValueSelected}
        />
    ),
);

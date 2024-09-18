/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import {
    SafeAreaView,
    ScrollView,
    StatusBar,
    StyleSheet,
    TouchableOpacity,
    Text,
    useColorScheme,
    View,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import {ChartsView, selectValue} from "./src/components/ChartsView";


const datasets = [
    {
        name: 'Red',
        data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        lineColor: '#FF0000',
        fillColor: '#FF0000',
        lineWidth: 2,
        drawFilledEnabled: true,
    },
    {
        name: 'Green',
        data: [2, 4, 6, 8, 10, 12, 14, 16, 18, 20],
        lineColor: '#00FF00',
        fillColor: '#00FF00',
        lineWidth: 2,
        drawFilledEnabled: true,
    },
];

function App(): React.JSX.Element {
    const isDarkMode = useColorScheme() === 'dark';

    const backgroundStyle = {
        backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
    };

    const chartRef = React.useRef(null);

    return (
        <SafeAreaView style={backgroundStyle}>
            <StatusBar
                barStyle={isDarkMode ? 'light-content' : 'dark-content'}
                backgroundColor={backgroundStyle.backgroundColor}
            />
            <ScrollView
                contentInsetAdjustmentBehavior="automatic"
                style={backgroundStyle}>
                <View
                    style={{
                        backgroundColor: isDarkMode ? Colors.black : Colors.white,
                    }}>
                    <ChartsView
                        ref={chartRef}
                        style={styles.chart}
                        datasets={datasets}
                        rotationEnabled={true}
                        legendEnabled={false}
                        webLineWidth={1}
                        innerWebLineWidth={1}
                        webColor={'#FF0000'}
                        innerWebColor={'#0000FF'}
                        onValueSelected={event => {
                            console.log('Value Selected Event', event.nativeEvent);
                        }}
                    />
                    <TouchableOpacity
                        onPress={() => {
                            selectValue(chartRef.current, 0, 3);
                        }}>
                        <Text>Select Red index 3</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        onPress={() => {
                            selectValue(chartRef.current, 1, 6);
                        }}>
                        <Text>Select Green index 6</Text>
                    </TouchableOpacity>
                </View>
            </ScrollView>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    sectionContainer: {
        marginTop: 32,
        paddingHorizontal: 24,
    },
    sectionTitle: {
        fontSize: 24,
        fontWeight: '600',
    },
    sectionDescription: {
        marginTop: 8,
        fontSize: 18,
        fontWeight: '400',
    },
    highlight: {
        fontWeight: '700',
    },
    chart: {
        width: '100%',
        height: 400,
    },
});

export default App;

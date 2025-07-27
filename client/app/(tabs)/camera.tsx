import { View, Text, TouchableOpacity } from 'react-native'
import React, { useState } from 'react'

import { CameraView, CameraType, useCameraPermissions } from 'expo-camera'
import Button from '@/components/Button';

const Camera = () => {
    const [facing, setFacing] = useState<CameraType>('front');
    const [permission, requestPermission] = useCameraPermissions();

    if (!permission) {
        // Camera permissions are still loading.
        return <View />;
    }

    if (!permission.granted) {
        // Camera permissions are not granted yet.
        return (
            <View>
                <Text>We need your permission to show the camera</Text>
                <Button onPress={requestPermission} label="grant permission" color='primary-400' />
            </View>
        )
    }

    function toggleCameraFacing() {
        setFacing(current => (current === 'back' ? 'front' : 'back'));
    }

    return (
        <View className='flex-1 justify-center bg-black'>
            <CameraView facing={facing} style={{
                flex: 1,
                position: "relative"
            }}>
                <View>
                    <TouchableOpacity onPress={toggleCameraFacing}>
                        <Text>Flip Camera</Text>
                    </TouchableOpacity>
                </View>
                <View className='absolute bottom-8 left-[50%] translate-x-[-50%] w-20 bg-primary-400 aspect-square rounded-full'>
                    {/* <Text className='text-white text-lg'>Take pic</Text> */}
                </View>
            </CameraView>
        </View>
    );
}

export default Camera
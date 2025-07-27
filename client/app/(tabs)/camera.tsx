import { View, Text, TouchableOpacity, Image } from 'react-native'
import React, { useRef, useState } from 'react'

import { CameraView, CameraType, useCameraPermissions } from 'expo-camera'
import Button from '@/components/Button'
import { icons } from '@/constants/icons'
import * as ImagePicker from 'expo-image-picker'

const Camera = () => {
    const [facing, setFacing] = useState<CameraType>('front')
    const cameraRef = useRef<CameraView>(null)

    const [permission, requestPermission] = useCameraPermissions()



    if (!permission) {
        // Camera permissions are still loading.
        return <View />
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



    const toggleCameraFacing = () => {
        setFacing(current => (current === 'back' ? 'front' : 'back'))
    }

    const takePic = async () => {
        const photo = await cameraRef.current?.takePictureAsync()
        console.log(photo)
    }

    const pickImage = async () => {
        // No permissions request is necessary for launching the image library
        let result = await ImagePicker.launchImageLibraryAsync({
            mediaTypes: ['images'],
            quality: 1,
        });

        console.log(result);
    }

    return (
        <View className='flex-1 justify-center bg-black'>
            <CameraView
                facing={facing}
                style={{
                    flex: 1,
                    position: "relative"
                }}
                ref={cameraRef}
                mode='picture'
            >
                <View className='absolute bottom-8 left-6 h-20 flex-row items-center'>
                    <TouchableOpacity onPress={pickImage} className='bg-white w-10 h-10 bg-light-200 rounded-[4px] overflow-hidden flex items-center justify-center'>
                        <Image
                            source={icons.gallery}
                            className='w-full h-full'
                            resizeMode='contain'
                        />
                    </TouchableOpacity>
                </View>
                <TouchableOpacity onPress={takePic} className='absolute bottom-8 left-[50%] translate-x-[-50%] w-20 bg-light-200 aspect-square rounded-full'>
                    <View className='relative w-full h-full'>
                        <View className='absolute w-16 bottom-[50%] left-[50%] translate-x-[-50%] translate-y-[50%] aspect-square rounded-full border-2 border-secondary-500' />
                    </View>
                </TouchableOpacity>

                <View className='w-20 aspect-square absolute bottom-8 right-6 flex-row items-center justify-end'>
                    <TouchableOpacity className='h-12 w-12 bg-light-200 aspect-square rounded-full p-[8px]' onPress={toggleCameraFacing}>
                        <Image
                            source={icons.flip}
                            className='w-full h-full'
                            resizeMode='contain'
                        />
                    </TouchableOpacity>
                </View>
            </CameraView>
        </View>
    );
}

export default Camera
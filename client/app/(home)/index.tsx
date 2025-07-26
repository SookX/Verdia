import { images } from '@/constants/images';
import { Image } from 'expo-image';
import { useRouter } from 'expo-router';
import { Button, ImageBackground, Platform, StyleSheet, Text, TouchableOpacity, View } from 'react-native';

export default function HomeScreen() {
  const router = useRouter()



  return (
    <ImageBackground className='flex-1 w-full justify-center items-center' source={images.home} resizeMode='cover'>
      <View className='flex-1 bg-black opacity-50 absolute z-0 w-full h-full'/>

      <View className='py-24 px-6 flex-1 w-full items-center'>
        <View className='flex-1 items-center justify-center'>
          <Text className="text-8xl font-bold text-center text-light-100 mb-6">Verdia</Text>
          <Text className='text-2xl font-bold text-center text-light-200'>Upload. Analyse. Cure.</Text>
          <Text className='text-2xl text-center text-light-200'>Keeping your plants safe today.</Text>
        </View>

        <TouchableOpacity className='w-full mb-3' onPress={() => router.push("/login")}>
          <View className='w-full py-3 px-6 rounded-full bg-primary-500'>
            <Text className='text-xl text-light-100 text-center uppercase font-bold'>Sign In</Text>
          </View>
        </TouchableOpacity>
        <Text className='text-lg text-light-100 text-center font-bold italic'>Create an account</Text>
      </View>
      </ImageBackground>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  stepContainer: {
    gap: 8,
    marginBottom: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: 'absolute',
  },
});

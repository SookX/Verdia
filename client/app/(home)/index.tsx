import Button from '@/components/Button';
import { images } from '@/constants/images';
import { useRouter } from 'expo-router';
import { ImageBackground, Platform, StyleSheet, Text, TouchableOpacity, View, Image } from 'react-native';

export default function HomeScreen() {
  const router = useRouter()



  return (
    <ImageBackground className='flex-1 w-full justify-center items-center' source={images.home} resizeMode='cover'>
      <View className='flex-1 bg-black opacity-50 absolute z-0 w-full h-full'/>

      <View className='py-24 px-6 flex-1 w-full items-center'>
        <View className='w-full flex-1 justify-center'>
          <View className='w-full flex items-center mb-6'>
            <Image
              source={images.logoHome}
              className='w-[75%] h-52'
              resizeMode='contain'
            />
          </View>

          <View className='mb-12'>
            <Text className="text-8xl font-bold text-center text-primary-500 mb-6">Verdia</Text>
            <Text className='text-2xl font-bold text-center text-light-200'>Upload. Analyse. Cure.</Text>
            <Text className='text-2xl text-center text-light-200'>Keeping your plants safe today.</Text>
          </View>
        </View>

        <Button styling='w-full mb-3' onPress={() => router.push("/login")} color="primary-500" label='Sign in'/>
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

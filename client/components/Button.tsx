import { View, Text, TouchableOpacity } from 'react-native'
import React from 'react'

interface ButtonInterface {
    color: string,
    label: string,
    styling?: string,
    onPress?: () => void
}

const Button = ({ color, label, styling, onPress } : ButtonInterface) => {
  return (
    <TouchableOpacity onPress={onPress} className={`py-3 px-6 rounded-full bg-${color} ${styling ? styling : ""}`}>
        <Text className='text-xl text-light-100 text-center uppercase font-bold'>{label}</Text>
    </TouchableOpacity>
  )
}

export default Button
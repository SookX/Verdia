import { View, Text } from 'react-native'
import React, { useEffect } from 'react'
import { crud } from '@/api/crud'

const Dashboard = () => {
    useEffect(() => {
        const fetchPlants = async () => {
            const response = await crud({
                url: "/plant",
                method: 'get'
            })

            console.log(JSON.stringify(response))
        }

        fetchPlants()
    }, [])


  return (
    <View>
      <Text>Dashboard</Text>
    </View>
  )
}

export default Dashboard
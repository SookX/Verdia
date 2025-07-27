import { Tabs } from 'expo-router';
import React from 'react';
import { useColorScheme } from '@/hooks/useColorScheme';

import "../global.css"

export default function TabLayout() {
  const colorScheme = useColorScheme();

  return (
    <Tabs>
      <Tabs.Screen
        name='camera'
        options={{
          title: "Camera",
          headerShown: false
        }}
      />
      <Tabs.Screen
        name='dashboard'
        options={{
          title: "Dashboard",
          headerShown: false
        }}
      />
    </Tabs>
  );
}

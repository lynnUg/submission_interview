from django.contrib.auth.models import User
from rest_framework import serializers

from webapp.models import GPS



#class roadtrackerserializer

class MobileSerializer(serializers.ModelSerializer):
    
    #the required forms ,we reduce on reusing forms by using this meta class
    class Meta:
        model = User
        fields = ('username', 'email')
#class roadtrackerserializer

class GPSSerializer(serializers.ModelSerializer):
    
    #the required forms ,we reduce on reusing forms by using this meta class
    class Meta:
        model = GPS
        fields = ('device_name', 'lat','longt')


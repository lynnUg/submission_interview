from django.db import models

class GPS(models.Model):
    device_name = models.CharField(max_length=100, blank=False)
    lat= models.CharField(max_length=100, blank=False)
    longt=models.CharField(max_length=100,blank=False)
    def __unicode__(self):
        return self.device_name
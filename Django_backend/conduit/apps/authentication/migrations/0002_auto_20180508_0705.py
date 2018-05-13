# Generated by Django 2.0.5 on 2018-05-08 07:05

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('authentication', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='user',
            name='is_active',
        ),
        migrations.RemoveField(
            model_name='user',
            name='is_staff',
        ),
        migrations.AddField(
            model_name='user',
            name='friends',
            field=models.CharField(db_index=True, default='', max_length=255),
        ),
        migrations.AddField(
            model_name='user',
            name='gender',
            field=models.IntegerField(default='0'),
        ),
        migrations.AddField(
            model_name='user',
            name='phone',
            field=models.CharField(db_index=True, default='', max_length=255, unique=True),
        ),
        migrations.AddField(
            model_name='user',
            name='verified',
            field=models.IntegerField(default='0'),
        ),
    ]
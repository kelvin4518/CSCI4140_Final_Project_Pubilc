from django.contrib import admin
from .models import Article,Comment,Tag,Blog

# Register your models here.
admin.site.register(Article)
admin.site.register(Comment)
admin.site.register(Tag)
admin.site.register(Blog)

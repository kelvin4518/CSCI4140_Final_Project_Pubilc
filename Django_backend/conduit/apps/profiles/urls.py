from django.conf.urls import url

from .views import ProfileRetrieveAPIView, ProfileFollowAPIView, ProfileRetrieveFollowersAPIView, ProfileRetrieveFolloweesAPIView, ArticlesMembersAPIView, ProfileUpdateAPIView


app_name = 'profiles'

urlpatterns = [
    url(r'^profiles/(?P<username>\w+)/?$', ProfileRetrieveAPIView.as_view()),
    url(r'^profile/upload/?$', ProfileUpdateAPIView.as_view()),
    url(r'^profiles/(?P<username>\w+)/follow/?$', ProfileFollowAPIView.as_view()),
    url(r'^profiles/(?P<username>\w+)/followers/?$', ProfileRetrieveFollowersAPIView.as_view()),
    url(r'^profiles/(?P<username>\w+)/followees/?$', ProfileRetrieveFolloweesAPIView.as_view()),
    url(r'^habits/members/?$', ArticlesMembersAPIView.as_view()),
]

from django.conf.urls import url
from .views import ProfileRetrieveAPIView, ProfileFollowAPIView, ProfileRetrieveFollowersAPIView, ProfileRetrieveFolloweesAPIView, ArticlesMembersAPIView, ProfileUpdateAPIView, ArticlesAuthorAPIView,ArticlesRankAPIView



app_name = 'profiles'

urlpatterns = [
    url(r'^profiles/?$', ProfileRetrieveAPIView.as_view()),
    url(r'^profile/upload/?$', ProfileUpdateAPIView.as_view()),
    url(r'^profiles/follow/?$', ProfileFollowAPIView.as_view()),
    url(r'^profiles/followers/?$', ProfileRetrieveFollowersAPIView.as_view()),
    url(r'^profiles/followees/?$', ProfileRetrieveFolloweesAPIView.as_view()),
    url(r'^habits/members/?$', ArticlesMembersAPIView.as_view()),
    url(r'^habits/author/?$', ArticlesAuthorAPIView.as_view()),
    url(r'^habits/ranks/?$', ArticlesRankAPIView.as_view()),
]

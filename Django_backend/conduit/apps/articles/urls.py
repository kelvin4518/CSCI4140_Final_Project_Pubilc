from django.conf.urls import include, url

from rest_framework.routers import DefaultRouter

from .views import (
    ArticleViewSet, ArticlesCreateAPIView,ArticlesUpdateAPIView,ArticlesDeleteAPIView,ArticlesShowAPIView, ArticlesFavoriteAPIView, ArticlesCreatedAPIView,
    ArticlesRegisteredAPIView, CommentsListCreateAPIView,CommentsListUpdateAPIView,CommentsRankAPIView,CommentsListShowAPIView, CommentsDestroyAPIView, TagListAPIView,
    ArticlesFeedAPIView, BlogsListCreateAPIView,BlogsListShowAPIView,BlogsListDeleteAPIView,BlogsListLikeAPIView, ArticlesOthersCreatedAPIView, 
    ArticlesOthersRegisteredAPIView, CommentsListIsAPIView


)

router = DefaultRouter(trailing_slash=False)
router.register(r'habit', ArticleViewSet)

app_name = 'articles'

urlpatterns = [
    url(r'^', include(router.urls)),

    url(r'^habits/show/?$', ArticlesShowAPIView.as_view({'post': 'show'})),
    url(r'^habits/create/?$', ArticlesCreateAPIView.as_view({'post': 'create'})),
    url(r'^habits/update/?$', ArticlesUpdateAPIView.as_view({'post': 'update'})),
    url(r'^habits/delete/?$', ArticlesDeleteAPIView.as_view({'post': 'delete'})),

    url(r'^habits/created/?$', ArticlesCreatedAPIView.as_view()),
    url(r'^habits/registered/?$', ArticlesRegisteredAPIView.as_view()),
    url(r'^habits/others_created/?$', ArticlesOthersCreatedAPIView.as_view({'post': 'show'})),
    url(r'^habits/others_registered/?$', ArticlesOthersRegisteredAPIView.as_view({'post': 'show'})),
    url(r'^habits/feed/?$', ArticlesFeedAPIView.as_view()),

    url(r'^habits/favorite/?$', ArticlesFavoriteAPIView.as_view()),

    url(r'^habits/check_create/?$', CommentsListCreateAPIView.as_view()),
    url(r'^habits/ischeck/?$', CommentsListIsAPIView.as_view({'post': 'show'})),

    url(r'^habits/check_update/?$', CommentsListUpdateAPIView.as_view()),
    url(r'^habits/check_calendar/?$', CommentsListShowAPIView.as_view({'post': 'show'})),
    url(r'^habits/rank/?$', CommentsRankAPIView.as_view({'post': 'list'})),

    url(r'^habits/blog_create/?$', BlogsListCreateAPIView.as_view()),
    url(r'^habits/blog_show/?$', BlogsListShowAPIView.as_view({'post': 'show'})),
    url(r'^habits/blog_delete/?$', BlogsListDeleteAPIView.as_view({'post': 'delete'})),
    url(r'^habits/blog_like/?$', BlogsListLikeAPIView.as_view()),
    
    url(r'^habits/(?P<article_slug>[-\w]+)/comments/(?P<comment_pk>[\d]+)/?$',CommentsDestroyAPIView.as_view()),

    url(r'^tags/?$', TagListAPIView.as_view()),
]

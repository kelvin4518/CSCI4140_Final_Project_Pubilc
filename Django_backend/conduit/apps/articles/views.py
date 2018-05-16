from rest_framework import generics, mixins, status, viewsets
from rest_framework.exceptions import NotFound
from rest_framework.permissions import (
    AllowAny, IsAuthenticated, IsAuthenticatedOrReadOnly
)
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework.views import APIView

from .models import Article, Comment, Tag, Blog
from .renderers import ArticleJSONRenderer, CommentJSONRenderer, BlogJSONRenderer, ProfileJSONRenderer
from .serializers import ArticleSerializer, CommentSerializer, BlogSerializer, TagSerializer
from django.http import JsonResponse
from django.db.models import Q


class ArticleViewSet(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):

    lookup_field = 'authorid'
    queryset = Article.objects.select_related('author', 'author__user')
    permission_classes = (IsAuthenticatedOrReadOnly,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer

    def get_queryset(self):
        queryset = self.queryset

        author = self.request.query_params.get('author', None)
        if author is not None:
            queryset = queryset.filter(author__user__username=author)
            #queryset = queryset.filter(authorid__user__username=author)
        tag = self.request.query_params.get('tag', None)
        if tag is not None:
            queryset = queryset.filter(tags__tag=tag)

        favorited_by = self.request.query_params.get('favorited', None)
        if favorited_by is not None:
            queryset = queryset.filter(
                favorited_by__user__username=favorited_by
            )

        return queryset

    def list(self, request):
        serializer_context = {'request': request}
        page = self.paginate_queryset(self.get_queryset())

        serializer = self.serializer_class(
            page,
            context=serializer_context,
            many=True
        )

        return self.get_paginated_response(serializer.data)

    def retrieve(self, request, authorid):
        serializer_context = {'request': request}
        # queryset = Article.objects.all()
        queryset = self.queryset.filter(authorid=authorid)
        page = self.paginate_queryset(queryset)

        serializer = self.serializer_class(
            page,
            context=serializer_context,
            many=True
        )

        return self.get_paginated_response(serializer.data)


class ArticlesShowAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    queryset = Article.objects.select_related('author', 'author__user')
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer
    def show(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        habitid = habit["habitid"]
        
        #return Response(serializer.data, status=status.HTTP_200_OK)
        try:
            serializer_instance = self.queryset.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug does not exist.')
            
        serializer = self.serializer_class(
            serializer_instance,
            context=serializer_context,
        )

        return Response(serializer.data, status=status.HTTP_200_OK)
        
class ArticlesCreateAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
                     
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer
    def create(self, request):
        serializer_context = {
            'author': request.user.profile,
            'request': request
        }
        serializer_data = request.data.get('habit', {})

        serializer = self.serializer_class(
        data=serializer_data, context=serializer_context
        )
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(serializer.data, status=status.HTTP_201_CREATED)
 
class ArticlesUpdateAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    lookup_field = 'habitid'
    queryset = Article.objects.select_related('author', 'author__user')
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer
    def update(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        habitid = habit["habitid"]
        
        #return Response(serializer.data, status=status.HTTP_200_OK)
        try:
            serializer_instance = self.queryset.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug does not exist.')
            
        #serializer_data = request.data.get('article', {})

        serializer = self.serializer_class(
            serializer_instance, 
            context=serializer_context,
            data=habit, 
            partial=True
        )
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(serializer.data, status=status.HTTP_200_OK)

class ArticlesDeleteAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    queryset = Article.objects.select_related('author', 'author__user')
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer
    def delete(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        habitid = habit["habitid"]
        
        #return Response(serializer.data, status=status.HTTP_200_OK)
        try:
            serializer_instance = self.queryset.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug does not exist.')
            
        #serializer_data = request.data.get('article', {})

        serializer_instance.delete()

        return JsonResponse({'success':'success'},status=200)

class CommentsListCreateAPIView(generics.ListCreateAPIView):
    lookup_field = 'article__slug'
    lookup_url_kwarg = 'article_slug'
    permission_classes = (IsAuthenticatedOrReadOnly,)
    queryset = Comment.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (CommentJSONRenderer,)
    serializer_class = CommentSerializer

    def create(self, request, article_slug=None):
        data = request.data.get('check', {})
        context = {'author': request.user.profile}
        habitid = data["habitid"]
        
        try:
            context['article'] = Article.objects.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug does not exist.')

        serializer = self.serializer_class(data=data, context=context)
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(serializer.data, status=status.HTTP_201_CREATED)


class CommentsListUpdateAPIView(generics.UpdateAPIView):
    lookup_field = 'article__slug'
    lookup_url_kwarg = 'article_slug'
    permission_classes = (IsAuthenticatedOrReadOnly,)
    queryset = Comment.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (CommentJSONRenderer,)
    serializer_class = CommentSerializer

    def update(self, request, article_slug=None):
        data = request.data.get('check', {})
        context = {'author': request.user.profile}
        habitid = data["habitid"]
        
        try:
            context['article'] = Article.objects.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug does not exist.')

        serializer_instance = Comment.objects.filter(article__id=habitid).get(author=request.user.profile)
        if data["body"] not in serializer_instance.body: 
            data['body'] = serializer_instance.body + "," + data["body"]
            newscore = int(serializer_instance.score)+1
            data['score'] = str(newscore)
            #serializer = self.serializer_class(data=data, context=context)
            serializer = self.serializer_class(
                serializer_instance, 
                context=context,
                data=data,
                partial=True
            )
            serializer.is_valid(raise_exception=True)
            serializer.save()
            # return JsonResponse({'success':'success'},status=200)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return JsonResponse({'check':'Already Checked!'},status=200)


class CommentsListIsAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    lookup_field = 'article__slug'
    lookup_url_kwarg = 'article_slug'
    permission_classes = (IsAuthenticatedOrReadOnly,)
    queryset = Comment.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (CommentJSONRenderer,)
    serializer_class = CommentSerializer

    def show(self, request, article_slug=None):
        data = request.data.get('check', {})
        context = {'author': request.user.profile}
        habitid = data["habitid"]
        
        try:
            context['article'] = Article.objects.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug does not exist.')

        serializer_instance = Comment.objects.filter(article__id=habitid).get(author=request.user.profile)
        if data["body"] not in serializer_instance.body: 
            return JsonResponse({'check':'Uncheck'},status=200)
        else:
            return JsonResponse({'check':'Checked'},status=200)
            
class CommentsListShowAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    lookup_field = 'article__slug'
    lookup_url_kwarg = 'article_slug'
    permission_classes = (IsAuthenticatedOrReadOnly,)
    queryset = Comment.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (CommentJSONRenderer,)
    serializer_class = CommentSerializer

    def show(self, request, article_slug=None):
        data = request.data.get('check', {})
        context = {'author': request.user.profile}
        habitid = data["habitid"]

        serializer_instance = Comment.objects.filter(article__id=habitid).get(author=request.user.profile)
        check_date = serializer_instance.body
        checks = check_date.split(',')
        
        return JsonResponse({'check_num':len(checks), 'check_date':checks},status=200)

        # serializer_context = {'request': request}

        # serializer = self.serializer_class(
            # serializer_instance,
            # context=serializer_context
        # )

        # return Response(serializer.data, status=status.HTTP_200_OK)
        
class CommentsRankAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    lookup_field = 'article__slug'
    lookup_url_kwarg = 'article_slug'
    permission_classes = (IsAuthenticatedOrReadOnly,)
    queryset = Comment.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (ProfileJSONRenderer,)

    serializer_class = CommentSerializer

    def filter_queryset(self, queryset):
        # The built-in list function calls `filter_queryset`. Since we only
        # want comments for a specific article, this is a good place to do
        # that filtering.
        filters = {self.lookup_field: self.kwargs[self.lookup_url_kwarg]}

        return queryset.filter(**filters)

    def list(self, request, article_slug=None):
        data = request.data.get('rank', {})
        context = {'author': request.user.profile}
        habitid = data["habitid"]

        serializer_instance = Comment.objects.filter(article__id=habitid)
        
        serializer_context = {'request': request}
        page = self.paginate_queryset(serializer_instance)

        serializer = self.serializer_class(
            page,
            context=serializer_context,
            many=True
        )

        return self.get_paginated_response(serializer.data)
        
class CommentsDestroyAPIView(generics.DestroyAPIView):
    lookup_url_kwarg = 'comment_pk'
    permission_classes = (IsAuthenticatedOrReadOnly,)
    queryset = Comment.objects.all()

    def destroy(self, request, article_slug=None, comment_pk=None):
        try:
            comment = Comment.objects.get(pk=comment_pk)
        except Comment.DoesNotExist:
            raise NotFound('A comment with this ID does not exist.')

        comment.delete()

        return Response(None, status=status.HTTP_204_NO_CONTENT)


class ArticlesFavoriteAPIView(APIView):
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer

    def delete(self, request, article_slug=None):
        profile = self.request.user.profile
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        habitid = habit["habitid"]
        try:
            article = Article.objects.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug was not found.')

        profile.unfavorite(article)

        serializer = self.serializer_class(article, context=serializer_context)

        return Response(serializer.data, status=status.HTTP_200_OK)

    def post(self, request, article_slug=None):
        profile = self.request.user.profile
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        habitid = habit["habitid"]
        try:
            article = Article.objects.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug was not found.')

        profile.favorite(article)

        serializer = self.serializer_class(article, context=serializer_context)

        return Response(serializer.data, status=status.HTTP_201_CREATED)


class TagListAPIView(generics.ListAPIView):
    queryset = Tag.objects.all()
    pagination_class = None
    permission_classes = (AllowAny,)
    serializer_class = TagSerializer

    def list(self, request):
        serializer_data = self.get_queryset()
        serializer = self.serializer_class(serializer_data, many=True)

        return Response({
            'tags': serializer.data
        }, status=status.HTTP_200_OK)


class ArticlesCreatedAPIView(generics.ListAPIView):
    permission_classes = (IsAuthenticated,)
    queryset = Article.objects.all()
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer

    def get_queryset(self):
        return Article.objects.filter(
            # author__in=self.request.user.profile.follows.all()
            author=self.request.user.profile
        )

    def list(self, request):
        queryset = self.get_queryset()
        page = self.paginate_queryset(queryset)

        serializer_context = {'request': request}
        serializer = self.serializer_class(
            page, context=serializer_context, many=True
        )

        return self.get_paginated_response(serializer.data)

class ArticlesRegisteredAPIView(generics.ListAPIView):
    permission_classes = (IsAuthenticated,)
    queryset = Article.objects.all()
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer

    def get_queryset(self):
        return Article.objects.filter(
            # author__in=self.request.user.profile.follows.all()
            favorited_by=self.request.user.profile
        ).exclude(
            author=self.request.user.profile
        )

    def list(self, request):
        queryset = self.get_queryset()
        page = self.paginate_queryset(queryset)

        serializer_context = {'request': request}
        serializer = self.serializer_class(
            page, context=serializer_context, many=True
        )

        return self.get_paginated_response(serializer.data)

class ArticlesFeedAPIView(generics.ListAPIView):
    permission_classes = (IsAuthenticated,)
    queryset = Article.objects.all()
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer

    def get_queryset(self):
        return Article.objects.all().exclude(
            favorited_by=self.request.user.profile
        ).exclude(
            author=self.request.user.profile    
        ).filter(
            is_public=1
        )

    def list(self, request):
        queryset = self.get_queryset()
        page = self.paginate_queryset(queryset)

        serializer_context = {'request': request}
        serializer = self.serializer_class(
            page, context=serializer_context, many=True
        )

        return self.get_paginated_response(serializer.data)

class BlogsListCreateAPIView(generics.ListCreateAPIView):
    permission_classes = (IsAuthenticated,)
    queryset = Comment.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (BlogJSONRenderer,)
    serializer_class = BlogSerializer

    #parser_classes = (MultiPartParser, FormParser)

    def create(self, request, article_slug=None):
        data = request.data.get('blog', {})
        context = {'author': request.user.profile}
        habitid = data["habitid"]
        
        try:
            context['article'] = Article.objects.get(id=habitid)
        except Article.DoesNotExist:
            raise NotFound('An article with this slug does not exist.')

        serializer = self.serializer_class(data=data, context=context, partial=True)

        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(serializer.data, status=status.HTTP_201_CREATED)
        
class BlogsListShowAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    permission_classes = (IsAuthenticated,)
    queryset = Blog.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (BlogJSONRenderer,)
    serializer_class = BlogSerializer
    #parser_classes = (MultiPartParser, FormParser)

    def show(self, request, article_slug=None):
        data = request.data.get('blog', {})

        habitid = data["habitid"]
        serializer_context = {'request': request}
        
        queryset = self.queryset.filter(article__id=habitid)
        page = self.paginate_queryset(queryset)

        serializer = self.serializer_class(
            page,
            context=serializer_context,
            many=True
        )

        return self.get_paginated_response(serializer.data)

class BlogsListDeleteAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    permission_classes = (IsAuthenticated,)
    queryset = Blog.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (BlogJSONRenderer,)
    serializer_class = BlogSerializer
    #parser_classes = (MultiPartParser, FormParser)
    
    def delete(self, request):
        data = request.data.get('blog', {})

        habitid = data["habitid"]
        serializer_context = {'request': request}
        
        serializer_instance = Blog.objects.filter(article__id=habitid).get(author=request.user.profile)

        serializer_instance.delete()

        return JsonResponse({'success':'success'},status=200)
        
class BlogsListLikeAPIView(generics.UpdateAPIView):
    permission_classes = (IsAuthenticated,)
    queryset = Blog.objects.select_related(
        'article', 'article__author', 'article__author__user',
        'author', 'author__user'
    )
    renderer_classes = (BlogJSONRenderer,)
    serializer_class = BlogSerializer
    #parser_classes = (MultiPartParser, FormParser)

    def update(self, request, article_slug=None):
        data = request.data.get('blog', {})
        #context = {'author': request.user.profile}
        habitid = data["habitid"]
        authorid = data["authorid"]

        serializer_instance = Blog.objects.filter(article__id=habitid).get(author__id=authorid)
        newlike = int(serializer_instance.like)+1
        data['like'] = str(newlike)
        #serializer = self.serializer_class(data=data, context=context)
        serializer = self.serializer_class(
            serializer_instance, 
            data=data,
            partial=True
        )
        serializer.is_valid(raise_exception=True)
        serializer.save()
        # return JsonResponse({'success':'success'},status=200)
        return Response(serializer.data, status=status.HTTP_201_CREATED)

class ArticlesOthersCreatedAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    queryset = Article.objects.all()
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer
    def show(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        authorid = habit["authorid"]
        queryset = self.queryset.filter(author__id=authorid).exclude(is_public=0)
        page = self.paginate_queryset(queryset)

        serializer = self.serializer_class(
            page,
            context=serializer_context,
            many=True
        )

        return self.get_paginated_response(serializer.data)
        
        
class ArticlesOthersRegisteredAPIView(mixins.CreateModelMixin, 
                     mixins.ListModelMixin,
                     mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    queryset = Article.objects.all()
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ArticleJSONRenderer,)
    serializer_class = ArticleSerializer
    def show(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        authorid = habit["authorid"]
        
        queryset = self.queryset.filter(favorited_by__id=authorid).exclude(is_public=0)
        page = self.paginate_queryset(queryset)

        serializer = self.serializer_class(
            page,
            context=serializer_context,
            many=True
        )

        return self.get_paginated_response(serializer.data)
        

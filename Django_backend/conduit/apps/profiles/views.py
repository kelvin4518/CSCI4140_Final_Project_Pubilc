from rest_framework import serializers, status, mixins, viewsets
from rest_framework.exceptions import NotFound
from rest_framework.generics import RetrieveAPIView,UpdateAPIView
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from django.db.models import Q

from .models import Profile

from .renderers import ProfileJSONRenderer
from .serializers import ProfileSerializer


class ProfileRetrieveAPIView(RetrieveAPIView):
    permission_classes = (IsAuthenticated,)
    queryset = Profile.objects.select_related('user')
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer

    def retrieve(self, request, *args, **kwargs):
        # Try to retrieve the requested profile and throw an exception if the
        # profile could not be found.
        #user = request.data.get('user', {});
        #userid = user["id"]
        try:
            profile = self.queryset.get(user=self.request.user)

        except Profile.DoesNotExist:
            raise NotFound('A profile with this username does not exist.')

        serializer = self.serializer_class(profile, context={
            'request': request
        })

        return Response(serializer.data, status=status.HTTP_200_OK)

class ProfileUpdateAPIView(APIView):
    permission_classes = (IsAuthenticated,)
    queryset = Profile.objects.select_related('user')
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request, *args, **kwargs):
        file_serializer = ProfileSerializer(self.request.user.profile,data=request.data,partial=True)
        if file_serializer.is_valid():
            file_serializer.save()
            return Response(file_serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(file_serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        # user_data = request.data.get('user', {})
        # serializer = self.serializer_class(
            # self.request.user.profile, data=user_data, partial=True
        # )
        # serializer.is_valid(raise_exception=True)
        # serializer.save()

        # return Response(serializer.data, status=status.HTTP_200_OK)


class ProfileFollowAPIView(APIView):
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer

    def delete(self, request):
        follower = self.request.user.profile
        user = request.data.get('user', {});
        userid = user["id"]
        try:
            followee = Profile.objects.get(user__id=userid)

        except Profile.DoesNotExist:
            raise NotFound('A profile with this username was not found.')

        follower.unfollow(followee)

        serializer = self.serializer_class(followee, context={
            'request': request
        })

        return Response(serializer.data, status=status.HTTP_200_OK)


    def post(self, request):
        follower = self.request.user.profile
        user = request.data.get('user', {});
        userid = user["id"]
        try:
            followee = Profile.objects.get(user__id=userid)

        except Profile.DoesNotExist:
            raise NotFound('A profile with this username was not found.')

        if follower.pk is followee.pk:
            raise serializers.ValidationError('You can not follow yourself.')

        follower.follow(followee)

        serializer = self.serializer_class(followee, context={
            'request': request
        })

        return Response(serializer.data, status=status.HTTP_201_CREATED)


class ProfileRetrieveFollowersAPIView(RetrieveAPIView):
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer
    queryset = Profile.objects.all()

    def retrieve(self, request, *args, **kwargs):
        queryset = self.queryset.filter(follows__user=self.request.user)

        followers = self.paginate_queryset(queryset)

        serializer = self.serializer_class(followers, many=True, context={
            'request': request
        })

        return self.get_paginated_response(serializer.data)
        # return Response(serializer.data, status=status.HTTP_200_OK)


class ProfileRetrieveFolloweesAPIView(RetrieveAPIView):
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer
    queryset = Profile.objects.all()

    def retrieve(self, request, *args, **kwargs):
        queryset = self.queryset.filter(followed_by__user=self.request.user)

        followees = self.paginate_queryset(queryset)

        serializer = self.serializer_class(followees, many=True, context={
            'request': request
        })

        return self.get_paginated_response(serializer.data)

class ArticlesMembersAPIView(RetrieveAPIView):
    queryset = Profile.objects.all()
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer
    def post(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        habitid = habit["habitid"]
        
        queryset = self.queryset.filter(favorites__id=habitid)
        followees = self.paginate_queryset(queryset)

        serializer = self.serializer_class(followees, many=True, context={
            'request': request
        })

        return self.get_paginated_response(serializer.data)
        
class ArticlesAuthorAPIView(RetrieveAPIView):
    queryset = Profile.objects.all()
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer
    def post(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('habit', {});
        habitid = habit["habitid"]
        
        
        queryset = self.queryset.filter(articles__id=habitid)
        followees = self.paginate_queryset(queryset)

        serializer = self.serializer_class(followees, many=True, context={
            'request': request
        })

        return self.get_paginated_response(serializer.data)
        
class ArticlesRankAPIView(RetrieveAPIView):
    queryset = Profile.objects.all()
    permission_classes = (IsAuthenticated,)
    renderer_classes = (ProfileJSONRenderer,)
    serializer_class = ProfileSerializer
    def post(self, request):
        serializer_context = {'request': request}
        habit = request.data.get('rank', {});
        habitid = habit["habitid"]
        
        queryset = self.queryset.filter(comments__article__id=habitid).order_by('-comments__score')
        followees = self.paginate_queryset(queryset)

        serializer = self.serializer_class(followees, many=True, context={
            'request': request
        })

        return self.get_paginated_response(serializer.data)

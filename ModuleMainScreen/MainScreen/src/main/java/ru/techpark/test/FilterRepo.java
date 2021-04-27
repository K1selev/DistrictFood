package ru.techpark.test;

public class FilterRepo {

    /*private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private final static MutableLiveData<List<Filter>> mLessons = new MutableLiveData<>();

    static {
        mLessons.setValue(Collections.<Filter>emptyList());
    }

    private final Context mContext;
    private FilterApi mLessonApi;

    public FilterRepo(Context context) {
        mContext = context;
        mLessonApi = ApiRepo.from(mContext).getLessonApi();
    }

    public LiveData<List<Filter>> getFilter() {
        return Transformations.map(mLessons, new Function<List<Filter>, List<Filter>>() {
            @Override
            public List<Filter> apply(List<Filter> input) {
                for (Filter filter : input) {
                    filter.setRating(9999);
                }
                return input;
            }
        });
    }

    public void refresh() {
        mLessonApi.getAll().enqueue(new Callback<List<FilterApi.FilterPlain>>() {
            @Override
            public void onResponse(Call<List<FilterApi.FilterPlain>> call,
                                   Response<List<FilterApi.FilterPlain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mLessons.postValue(transform(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<FilterApi.FilterPlain>> call, Throwable t) {
                Log.e("LessonRepo", "Failed to load", t);
            }
        });
    }

    public void like(final Filter filter) {
        mLessonApi.like(filter.getId(), new FilterApi.Like(filter.getRating() + 1)).enqueue(new Callback<FilterApi.Like>() {
            @Override
            public void onResponse(Call<FilterApi.Like> call,
                                   Response<FilterApi.Like> response) {
                if (response.isSuccessful()) {
                    refresh();
                    // todo refreshSingle() in production :)
                }
            }

            @Override
            public void onFailure(Call<FilterApi.Like> call, Throwable t) {
                Log.d("Test", "Failed to like " + filter.getId(), t);
                t.printStackTrace();
            }
        });
    }

    private static List<Filter> transform(List<FilterApi.FilterPlain> plains) {
        List<Filter> result = new ArrayList<>();
        for (FilterApi.FilterPlain filterPlain : plains) {
            try {
                Filter filter = map(filterPlain);
                result.add(filter);
                Log.e("LessonRepo", "Loaded " + filter.getName() + " #" + filter.getId());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static Filter map(FilterApi.FilterPlain lessonPlain) throws ParseException {
        return new Filter(
                lessonPlain.id,
                lessonPlain.name,
                sSimpleDateFormat.parse(lessonPlain.date),
                lessonPlain.place,
                lessonPlain.is_rk,
                lessonPlain.rating
        );
    }*/

}

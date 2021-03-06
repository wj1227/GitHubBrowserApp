package com.omjoonkim.app.githubBrowserApp

import com.omjoonkim.app.githubBrowserApp.ui.repo.RepoDetailPresenter
import com.omjoonkim.app.githubBrowserApp.ui.repo.RepoDetailView
import com.omjoonkim.app.githubBrowserApp.viewmodel.MainViewModel
import com.omjoonkim.app.githubBrowserApp.viewmodel.SearchViewModel
import com.omjoonkim.project.githubBrowser.data.interactor.ForkDataRepository
import com.omjoonkim.project.githubBrowser.data.interactor.RepoDataRepository
import com.omjoonkim.project.githubBrowser.data.interactor.UserDataRepository
import com.omjoonkim.project.githubBrowser.data.source.GithubBrowserRemote
import com.omjoonkim.project.githubBrowser.domain.interactor.usecases.GetRepoDetail
import com.omjoonkim.project.githubBrowser.domain.interactor.usecases.GetUserData
import com.omjoonkim.project.githubBrowser.domain.repository.ForkRepository
import com.omjoonkim.project.githubBrowser.domain.repository.RepoRepository
import com.omjoonkim.project.githubBrowser.domain.repository.UserRepository
import com.omjoonkim.project.githubBrowser.domain.schedulers.SchedulersProvider
import com.omjoonkim.project.githubBrowser.remote.GithubBrowserRemoteImpl
import com.omjoonkim.project.githubBrowser.remote.GithubBrowserServiceFactory
import com.omjoonkim.project.githubBrowser.remote.mapper.ForkEntityMapper
import com.omjoonkim.project.githubBrowser.remote.mapper.RepoEntityMapper
import com.omjoonkim.project.githubBrowser.remote.mapper.UserEntityMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val myModule: Module = module {
    //presentation
    viewModel { (id: String) -> MainViewModel(id, get(), get()) }
    viewModel { SearchViewModel(get()) }
    factory { (view: RepoDetailView) -> RepoDetailPresenter(view, get()) }

    //app
    single { Logger() }
    single { AppSchedulerProvider() as SchedulersProvider }

    //domain
    factory { GetUserData(get(), get(), get()) }
    factory { GetRepoDetail(get(), get(), get()) }

    //data
    single { UserDataRepository(get()) as UserRepository }
    single { RepoDataRepository(get()) as RepoRepository }
    single { ForkDataRepository(get()) as ForkRepository }

    //remote
    single { GithubBrowserRemoteImpl(get(), get(), get(), get()) as GithubBrowserRemote }
    single { RepoEntityMapper() }
    single { UserEntityMapper() }
    single { ForkEntityMapper(get()) }
    single {
        GithubBrowserServiceFactory.makeGithubBrowserService(
            BuildConfig.DEBUG,
            "https://api.github.com"
        )
    }
}

//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Ashish Mohite + on 10/06/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension HomeScreen {
    @MainActor class HomeViewModel: ObservableObject {
        private let getPodcasts = BestPodcastsStore.init()
        
        @Published private(set) var podcasts: [Podcast] = []
        @Published private(set) var isLoading: Bool = false
        private var currentPage = 1
        private(set) var loadFinished: Bool = false
        
        func loadPodcasts() async {
            if isLoading { return }
            
            do {
                for await podcasts in getPodcasts.invoke(query: GetGenreQuery(genreId: nil, page: Int32(currentPage))) {
//                    let podcasts = try await getPodcasts.invoke(query: GetGenreQuery(genreId: nil, page: Int32(1)))
                    isLoading = false
                    loadFinished = podcasts.isEmpty
                    currentPage += 1
                    self.podcasts = self.podcasts + podcasts
                }
            } catch  {
                isLoading = false
                loadFinished = true
                print(error.localizedDescription)
            }
        }
        
    }
}

//
//  PodcastDetailViewModel.swift
//  iosApp
//
//  Created by Ashish Mohite + on 11/06/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension PodcastDetailScreen {
    @MainActor class PodcastDetailViewModel: ObservableObject {
        @Published private(set) var podcast: Podcast? = nil
        @Published private(set) var isLoading: Bool = false
        
        private let getPodcast = GetPodcast.init()
        
        func loadPodcast(id: String) {
            Task.init(){
                await loadPodcastAsync(id: id)
            }
        }
        
        func loadPodcastAsync(id: String) async {
            if isLoading { return }
            isLoading = true
            
            do {
                let podcast = try await getPodcast.invoke(id: id)
                isLoading = false
                self.podcast = podcast
            } catch  {
                isLoading = false
                print(error.localizedDescription)
            }
        }
    }
}

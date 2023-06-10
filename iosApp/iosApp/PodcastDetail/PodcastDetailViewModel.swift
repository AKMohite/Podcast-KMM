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
    @MainActor class PodcastDetailViewModel {
        @Published private(set) var podcast: Podcast? = nil
        @Published private(set) var isLoading: Bool = false
    }
}
